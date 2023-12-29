package com.ngapak.dev.colorize.util

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.buffer

class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val progressListener: (bytesDownloaded: Long, totalBytes: Long) -> Unit
) : ResponseBody() {

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        return object : ForwardingSource(responseBody.source()) {
            var totalBytesRead = 0L
            var totalFileBytes = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                if (bytesRead != -1L) {
                    totalBytesRead += bytesRead
                }
                if (totalFileBytes == 0L) {
                    totalFileBytes = contentLength()
                }
                progressListener(totalBytesRead, totalFileBytes)
                return bytesRead
            }
        }.buffer()
    }
}