package me.donnie.reader.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Service
import android.app.backup.BackupAgent
import android.content.Context
import android.content.ContextWrapper

val appCtx: Context get() = internalCtx ?: initAndGetAppCtxWithReflection()

@SuppressLint("StaticFieldLeak")
private var internalCtx: Context? = null

fun Context.injectAsAppCtx() {
  require(!canLeakMemory()) { "The passed Context($this) would leak memory!" }
  internalCtx = this
}

fun Context.canLeakMemory(): Boolean = when (this) {
  is Application -> false
  is Activity, is Service, is BackupAgent -> true
  is ContextWrapper -> if (baseContext === this) true else baseContext.canLeakMemory()
  else -> applicationContext === null
}

@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
private fun initAndGetAppCtxWithReflection(): Context {
  // Fallback, should only run once per non default process.
  val activityThread = Class.forName("android.app.ActivityThread")
  val ctx = activityThread.getDeclaredMethod("currentApplication").invoke(null) as Context
  internalCtx = ctx
  return ctx
}