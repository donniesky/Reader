package me.donnie.reader.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import java.util.concurrent.atomic.AtomicInteger

val Int.dp: Int
  get() = (this * Resources.getSystem().displayMetrics.density).toInt()

inline val View.matchParent
  get() = ViewGroup.LayoutParams.MATCH_PARENT

inline val View.wrapContent
  get() = ViewGroup.LayoutParams.WRAP_CONTENT

fun View.visible() {
  this.visibility = View.VISIBLE
}

fun View.inVisible() {
  this.visibility = View.INVISIBLE
}

fun View.gone() {
  this.visibility = View.GONE
}

fun View.assignAndGetGeneratedId(): Int = generateViewId().also { generatedId ->
  id = generatedId
  isSaveEnabled = false // New id will be generated, so can't restore saved state.
}

val View.existingOrNewId: Int
  get() = id.let { currentId ->
    if (currentId == View.NO_ID) assignAndGetGeneratedId() else currentId
  }

fun generateViewId(): Int = when {
  isMainThread -> mainThreadLastGeneratedId.also {
    // Decrement here to avoid any collision with other generated ids which are incremented.
    mainThreadLastGeneratedId = (if (it == 1) aaptIdsStart else it) - 1
  }
  Build.VERSION.SDK_INT >= 17 -> View.generateViewId()
  else -> generatedViewIdCompat()
}

private const val aaptIdsStart = 0x00FFFFFF
private var mainThreadLastGeneratedId = aaptIdsStart - 1

private val nextGeneratedId = AtomicInteger(1)
private fun generatedViewIdCompat(): Int {
  while (true) {
    val result = nextGeneratedId.get()
    // aapt-generated IDs have the high byte nonzero. Clamp to the range under that.
    var newValue = result + 1
    if (newValue > aaptIdsStart) newValue = 1 // Roll over to 1, not 0.
    if (nextGeneratedId.compareAndSet(result, newValue)) {
      return result
    }
  }
}

inline fun FrameLayout.frameLayoutParams(
  width: Int = wrapContent,
  height: Int = wrapContent,
  @SuppressLint("InlinedApi")
  gravity: Int = FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY,
  initParams: FrameLayout.LayoutParams.() -> Unit = {}
): FrameLayout.LayoutParams {
  return FrameLayout.LayoutParams(width, height).also {
    it.gravity = gravity
  }.apply(initParams)
}