package com.example.my_testappkg

import android.app.Application


import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppTestingApplication : Application(){
    override fun onCreate() {
        super.onCreate()

    }
}