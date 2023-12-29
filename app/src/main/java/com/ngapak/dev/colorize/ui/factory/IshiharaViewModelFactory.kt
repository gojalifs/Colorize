package com.ngapak.dev.colorize.ui.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ngapak.dev.colorize.data.repository.IshiharaRepository
import com.ngapak.dev.colorize.injection.Injection
import com.ngapak.dev.colorize.ui.screen.ishihara_test.IshiharaViewModel

class IshiharaViewModelFactory(private val repository: IshiharaRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IshiharaViewModel::class.java)) {
            return IshiharaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: IshiharaViewModelFactory? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: IshiharaViewModelFactory(Injection.provideIshiharaRepository(context))
        }.also { instance = it }
    }
}