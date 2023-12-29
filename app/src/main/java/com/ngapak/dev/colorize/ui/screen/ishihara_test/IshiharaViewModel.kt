package com.ngapak.dev.colorize.ui.screen.ishihara_test

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ngapak.dev.colorize.data.local.entities.IshiharaEntities
import com.ngapak.dev.colorize.data.repository.IshiharaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class IshiharaViewModel(private val repository: IshiharaRepository) : ViewModel() {
    private val _isImageDownloaded = MutableStateFlow<Boolean?>(null)
    val isImageDownloaded: StateFlow<Boolean?> = _isImageDownloaded

    private val _allTestData = MutableStateFlow<List<IshiharaEntities>>(listOf())

    private val _urls = MutableStateFlow<List<String>>(listOf())

    private val _downloadProgress = MutableStateFlow(0)
    val downloadProgress: StateFlow<Int> = _downloadProgress

    private val _answer = mutableMapOf<Int, Boolean>()
    val answer: Map<Int, Boolean> = _answer

    fun addAnswer(id: Int, answer: Boolean) {
        _answer[id] = answer
    }

    fun downloadData(context: Context) {
        viewModelScope.launch {
            _isImageDownloaded.value = null
            repository.downloadData(context, _urls.value).collect { status ->
                _isImageDownloaded.value = status
            }
        }
    }

    fun getAllData() {
        viewModelScope.launch {
            repository.getAllData().collect {
                _allTestData.value = it
                val urls = mutableListOf<String>()
                it.forEach { data ->
                    urls.add(data.imgUrl)
                }
                _urls.value = urls
            }
            repository.checkData()
                .onStart { _isImageDownloaded.value = null }
                .catch { _isImageDownloaded.value = false }
                .collect { status ->
                    _isImageDownloaded.value = status
                }
        }
    }

    val ishiharaTests = repository.getTestData().cachedIn(viewModelScope)
}