package com.ngapak.dev.colorize.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ngapak.dev.colorize.R
import com.ngapak.dev.colorize.data.local.dao.IshiharaDao
import com.ngapak.dev.colorize.data.local.entities.IshiharaEntities
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.concurrent.Executors

@Database(
    entities = [IshiharaEntities::class],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)
abstract class ColorizeDatabase : RoomDatabase() {
    abstract fun ishiharaDao(): IshiharaDao

    companion object {
        @Volatile
        private var INSTANCE: ColorizeDatabase? = null

        fun getInstance(context: Context): ColorizeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ColorizeDatabase::class.java,
                    "colorize.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Executors.newSingleThreadExecutor().execute {
                                fillWithStartingData(context, getInstance(context).ishiharaDao())
                            }
                        }
                    }).build()
                INSTANCE = instance
                instance
            }
        }

        private fun fillWithStartingData(context: Context, dao: IshiharaDao) {
            val ishihara = loadJsonArray(context)
            try {
                if (ishihara != null) {
                    for (i in 0 until ishihara.length()) {
                        val item = ishihara.getJSONObject(i)
                        dao.insertData(
                            IshiharaEntities(
                                id = item.getInt("id"),
                                imgUrl = item.getString("imgUrl"),
                                answerTrue = item.getString("answerTrue"),
                                answerFalse = item.getString("answerFalse"),
                                protan = item.getString("protan"),
                                deutan = item.getString("deutan"),
                            )
                        )
                    }
                }
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
        }

        private fun loadJsonArray(context: Context): JSONArray? {
            val builder = StringBuilder()
            val `in` = context.resources.openRawResource(R.raw.ishihara)
            val reader = BufferedReader(InputStreamReader(`in`))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    builder.append(line)
                }
                val json = JSONObject(builder.toString())
                return json.getJSONArray("ishihara")
            } catch (exception: IOException) {
                exception.printStackTrace()
            } catch (exception: JSONException) {
                exception.printStackTrace()
            }
            return null
        }
    }
}