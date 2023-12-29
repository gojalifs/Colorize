package com.ngapak.dev.colorize.data.repository

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ngapak.dev.colorize.data.local.dao.IshiharaDao
import com.ngapak.dev.colorize.data.local.entities.IshiharaEntities
import com.ngapak.dev.colorize.data.paging_source.IshiharaPagingSource
import com.ngapak.dev.colorize.util.ProgressResponseBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import java.io.File
import java.util.concurrent.TimeUnit
import java.util.zip.ZipFile

class IshiharaRepository(private val dao: IshiharaDao) {
    fun getAllData() = flow {
        val data = dao.getIshiharaTestData()
        emit(data)
    }

    fun getTestData(): Flow<PagingData<IshiharaEntities>> {
        return Pager(
            config = PagingConfig(pageSize = 1),
            pagingSourceFactory = { IshiharaPagingSource(dao) }
        ).flow
    }

    fun checkData() = flow {
        val ishiharaEntities = dao.getIshiharaTestData()
        val result = !ishiharaEntities.any { it.imgPath == null }
        emit(result)
    }

    suspend fun downloadData(context: Context, urls: List<String>) = flow {
        val cacheDir = context.cacheDir
        emit(null)
        /// TODO add download progress handling

        try {
            coroutineScope {
                val client = OkHttpClient.Builder()
                    .callTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

                val paths = mutableListOf<String>()

                for (i in urls.indices) {

                    withContext(Dispatchers.IO) {
                        val request = Request.Builder().url(urls[i]).build()
                        val response = client.newCall(request).execute()
                        val responseHeaders = response.headers
                        val contentDisposition = responseHeaders["Content-Disposition"]
                        val fileName = contentDisposition?.let { disposition ->
                            val startIndex = disposition.indexOf("filename=")
                            if (startIndex >= 0) {
                                val endIndex = disposition.indexOf(";", startIndex)
                                if (endIndex >= 0) {
                                    disposition.substring(startIndex + 9, endIndex)
                                } else {
                                    disposition.substring(startIndex + 9)
                                }
                            } else {
                                null
                            }
                        } ?: "downloaded_file.png" // If filename not found, use a default name

                        val file = File(cacheDir, fileName)

                        file.outputStream().use { fileOut ->
                            response.body?.byteStream()?.copyTo(fileOut)
                        }

                        // Download successful, get the path
                        /// TODO nanti dibuat list filepath saja, biar bisa revert
                        val filePath = file.absolutePath
                        // Use the filePath for further operations
                        paths.add(filePath)
                        response.close()
                    }
                }

                if (paths.size == urls.size) {
                    for (i in paths.indices) {
                        dao.updateDataById(i + 1, paths[i])
                    }
                    emit(true)
                } else {
                    emit(false)
                }

            }

        } catch (e: Exception) {
            // Handle download errors
            Log.e("TAG", "downloadData: ${e.message} ${e.printStackTrace()} ${e.stackTrace}")
            emit(false)
        }
    }

    /// TODO Future development
    suspend fun downloadIshiharaFile(
        url: String,
        context: Context,
        progressListener: (bytesDownloaded: Long, totalBytes: Long) -> Unit
    ) {
        val zipFile = downloadZipFile(url, context, progressListener)
        zipFile?.let {
            val outputDir = File(context.applicationContext.cacheDir, "ishihara_images")
            if (extractZipFile(it, outputDir)) {
                // Extraction successful, do further processing if needed
            } else {
                // Extraction failed, handle error
                Log.e("TAG", "downloadIshiharaFile: extract failed")
            }
        }
    }

    private suspend fun downloadZipFile(
        url: String,
        context: Context,
        progressListener: (bytesDownloaded: Long, totalBytes: Long) -> Unit
    ): File? {
        val request = Request.Builder().url(url).build()

        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient.Builder()
                    .addNetworkInterceptor { chain ->
                        val originalResponse = chain.proceed(chain.request())
                        originalResponse.newBuilder()
                            .body(originalResponse.body?.let {
                                ProgressResponseBody(
                                    it,
                                    progressListener
                                )
                            })
                            .build()
                    }
                    .build()

                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    // Handle the error
                    return@withContext null
                }

                val cacheDir = context.cacheDir
                val zipFile = File(cacheDir, "downloaded_file.zip")
                val sink = zipFile.sink().buffer()

                response.body?.let { responseBody ->
                    val source = responseBody.source()
                    var totalBytesRead: Long = 0
                    var bytesRead: Long
                    val bufferSize = 8 * 1024 // Adjust buffer size as needed

                    while (source.read(sink.buffer, bufferSize.toLong())
                            .also { bytesRead = it } != -1L
                    ) {
                        sink.emit()
                        totalBytesRead += bytesRead
                        progressListener(totalBytesRead, responseBody.contentLength())
                    }
                }

                sink.close()
                return@withContext zipFile
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }

    private fun extractZipFile(zipFile: File, outputDir: File): Boolean {
        try {
            val zip = ZipFile(zipFile)
            val entries = zip.entries()

            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                val entryFile = File(outputDir, entry.name)

                if (entry.isDirectory) {
                    entryFile.mkdirs()
                } else {
                    val entryInputStream = zip.getInputStream(entry)
                    entryFile.outputStream().use { output ->
                        entryInputStream.copyTo(output)
                    }
                    entryInputStream.close()
                }
            }

            zip.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: IshiharaRepository? = null

        fun getInstance(dao: IshiharaDao) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: IshiharaRepository(dao)
        }.also { INSTANCE = it }
    }
}