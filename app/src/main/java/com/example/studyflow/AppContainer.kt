package com.example.studyflow

import android.app.Application

class AppContainer : Application() {
    lateinit var someRepository: SomeRepository
    override fun onCreate() {
        super.onCreate()
        someRepository = SomeRepository()
    }
}