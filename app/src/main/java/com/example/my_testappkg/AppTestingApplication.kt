package com.example.my_testappkg

import android.app.Application


import dagger.hilt.android.HiltAndroidApp

//class AppTestingApplication : Application() {
//    override fun onCreate() {
//        super.onCreate()
////        val dependenciesModuleList = dependenciesModuleList
//        startKoin {
//            androidLogger()
//            androidContext(this@AppTestingApplication)
//            if (BuildConfig.DEBUG) androidLogger(Level.ERROR)
//            modules(dependenciesModuleList)
//        }
//    }
//}
//val dependenciesModuleList = listOf(apiServiceModule,interactionModule ,viewModelModule
//
//)
//
@HiltAndroidApp
class AppTestingApplication : Application(){
    override fun onCreate() {
        super.onCreate()

    }
}