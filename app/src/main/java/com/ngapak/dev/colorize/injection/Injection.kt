package com.ngapak.dev.colorize.injection

import android.content.Context
import com.ngapak.dev.colorize.data.local.ColorizeDatabase
import com.ngapak.dev.colorize.data.repository.IshiharaRepository

object Injection {
    fun provideIshiharaRepository(context: Context): IshiharaRepository {
        val db = ColorizeDatabase.getInstance(context)
        return IshiharaRepository(db.ishiharaDao())
    }
}