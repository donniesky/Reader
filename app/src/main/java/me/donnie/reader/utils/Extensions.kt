@file:Suppress("NOTHING_TO_INLINE")
package me.donnie.reader.utils

import android.content.Context
import android.content.res.ColorStateList
import android.os.Looper
import androidx.annotation.AttrRes
import androidx.appcompat.content.res.AppCompatResources

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

fun Context.getColorFromAttrRes(
  @AttrRes attrRes: Int,
  defaultValue: Int
) : Int {
  val colorStateList = getColorStateListFromAttrRes(attrRes) ?: return defaultValue
  return colorStateList.defaultColor
}

fun Context.getColorStateListFromAttrRes(
  @AttrRes attrRes: Int
) : ColorStateList? {
  val a = obtainStyledAttributes(intArrayOf(attrRes))
  try {
    val resId = a.getResourceId(0, 0)
    if (resId != 0) {
      return AppCompatResources.getColorStateList(this, resId)
    }
    return null
  } finally {
    a.recycle()
  }
}