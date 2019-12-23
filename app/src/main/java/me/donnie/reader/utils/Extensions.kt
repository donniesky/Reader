@file:Suppress("NOTHING_TO_INLINE")
package me.donnie.reader.utils

import android.os.Looper

@JvmField
val mainLooper: Looper = Looper.getMainLooper()

@JvmField
val mainThread: Thread = mainLooper.thread

val isMainThread
  inline get() = mainThread === Thread.currentThread()

inline fun checkMainThead() = check(isMainThread) {
  "This should ONLY be called on the main thread! Current: ${Thread.currentThread()}"
}

inline fun checkNotMainThread() = check(!isMainThread) {
  "This should NEVER be called on the main thread! Current: ${Thread.currentThread()}"
}