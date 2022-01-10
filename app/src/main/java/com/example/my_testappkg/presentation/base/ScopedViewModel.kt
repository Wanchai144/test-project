package com.example.my_testappkg.presentation.base

import androidx.lifecycle.ViewModel
import com.example.my_testappkg.BuildConfig
import com.example.my_testappkg.extention.LaunchSafe
import kotlinx.coroutines.*


import kotlin.coroutines.CoroutineContext


open class ScopedViewModel : ViewModel(), CoroutineScope, LaunchSafe {

    private val job = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + job + safeCoroutineExceptionHandler())

//    val errorLiveEvent = LiveEvent<Throwable>()

    override fun onCleared() {
        uiScope.coroutineContext.cancelChildren()
        super.onCleared()
    }

    override val coroutineContext: CoroutineContext
        get() = uiScope.coroutineContext

    override fun onCancellation(e: CancellationException) {
        // DO NOTHING
    }

    override fun onFailure(t: Throwable) {
//        errorLiveEvent.postValue(t)
    }

    private fun safeCoroutineExceptionHandler() = CoroutineExceptionHandler { context, throwable ->
        if (BuildConfig.DEBUG) {
            Thread.currentThread()
                .uncaughtExceptionHandler
                ?.uncaughtException(Thread.currentThread(), throwable)
        } else {

            val handlingExceptionMap = mapOf(
                "Key" to "ScopedViewModel",
                "Value" to "Problem with Coroutine caused by ${throwable.message} in context $context"
            )

        }
    }
}
