package com.example.studyflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val someRepository: SomeRepository) : ViewModel() {
    val textStateFlow = MutableStateFlow("")

    var textSharedFlow = MutableSharedFlow<Int>()

    init {
        viewModelScope.launch {
            launch {
                someRepository.getTextDataStateFlow().onEach { data ->
                    textStateFlow.emit(data)
                }.launchIn(CoroutineScope(Dispatchers.Main))
            }
            launch {
                someRepository.getDataSharedFlow().onEach { data ->
                    textSharedFlow.emit(data)
                    delay(1000)
                }
                    .launchIn(CoroutineScope(Dispatchers.Main))
            }
        }
    }

    fun getFlow() = someRepository.getTextDataFlow()
}