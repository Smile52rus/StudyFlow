package com.example.studyflow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class SomeRepository {
    private val textDataFlow = flow {
        var i = 0
        while (true) {
            emit(i)
            i++
            delay(100)
        }
    }
        .flowOn(Dispatchers.Default)

    fun getTextDataFlow(): Flow<Int> {
        return textDataFlow
    }

//////////////////

    private val textStateFlow: StateFlow<String> = flow {
        emit("\n Этот текст мы запросили и получили через 5 секунд 1 раз при запуске через StateFlow")
    }
        .stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.Eagerly, "")

    suspend fun getTextDataStateFlow(): StateFlow<String> {
        delay(3000)
        return textStateFlow
    }
/////////////////


    private val textSharedFlow: SharedFlow<Int> = flow {
        var i = 0
        while (true) {
            emit(i)
            i++
            delay(10)
        }
    }//.buffer(50, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        .shareIn(CoroutineScope(Dispatchers.Default), SharingStarted.Eagerly)

    fun getDataSharedFlow(): SharedFlow<Int> {
        return textSharedFlow
    }
}