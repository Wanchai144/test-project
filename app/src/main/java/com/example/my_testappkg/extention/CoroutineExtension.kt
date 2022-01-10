package com.example.my_testappkg.extention

import androidx.annotation.WorkerThread
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launchSafe(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    launchSafe: LaunchSafe = object : LaunchSafe {
        override fun onCancellation(e: CancellationException) {

            val handlingExceptionMap = mapOf(
                "Key" to "CoroutineScopeExtension.launchSafe.onCancellation()",
                "Value" to "Problem with Coroutine caused by $e in context $context"
            )


        }

        override fun onFailure(t: Throwable) {

            val handlingExceptionMap = mapOf(
                "Key" to "CoroutineScopeExtension.launchSafe.onFailure()",
                "Value" to "Problem with Coroutine caused by ${t.message} in context $context"
            )

        }
    },
    onStart: (suspend CoroutineScope.() -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
) = launch(context, start) {
    try {
        onStart?.invoke(this)
        block()
    } catch (e: CancellationException) {
        try {
            launchSafe.onCancellation(e)
        } catch (t: Throwable) {
            launchSafe.onFailure(t)
        }
    } catch (t: Throwable) {
        launchSafe.onFailure(t)
    }
}

fun <T> Flow<T>.launchSafeIn(scope: CoroutineScope): Job = scope.launchSafe {
    collect() // tail-call
}

suspend inline fun <T> Flow<T>.collectSafe(
    collectSafe: CollectSafe = object : CollectSafe {
        override fun onCancellation(e: CancellationException) {

            val handlingExceptionMap = mapOf(
                "Key" to "CoroutineScopeExtension.collectSafe.onCancellation()",
                "Value" to "Problem with Flow caused by ${e.message}"
            )
        }
    },
    crossinline action: suspend (value: T) -> Unit
): Unit =
    collect { value ->
        try {
            action(value)
        } catch (error: CancellationException) {
            collectSafe.onCancellation(error)
        }
    }

interface LaunchSafe {

    @WorkerThread
    fun onCancellation(e: CancellationException)

    @WorkerThread
    fun onFailure(t: Throwable)
}

interface CollectSafe {

    @WorkerThread
    fun onCancellation(e: CancellationException)
}
