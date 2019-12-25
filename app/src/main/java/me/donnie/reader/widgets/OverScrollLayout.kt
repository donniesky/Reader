package me.donnie.reader.widgets

import android.animation.Animator
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

class OverScrollLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {
  
  private val OVER_SCROLL_THRESHOLD_RATIO = 0.20f
  private val RESTORE_ANIM_DURATION = 100
  private val MINIMUM_OVER_SCROLL_SCALE = 0.99f
  
  private var overScrollThreshold: Int = 0
  
  private var originalRect = Rect()
  
  var overScrollListener: OnOverScrollListener? = null
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    super.onLayout(changed, l, t, r, b)
    originalRect.set(l, t, r, b)
    overScrollThreshold = (originalRect.height() * OVER_SCROLL_THRESHOLD_RATIO).toInt()
  }
  
  override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
    if (y.toInt() != originalRect.top) {
      val scale = Math.max(MINIMUM_OVER_SCROLL_SCALE, 1 - (Math.abs(y) / overScrollThreshold) * (1 - MINIMUM_OVER_SCROLL_SCALE))
      scaleX = scale
      scaleY = scale
      translationY(-dy.toFloat())
      consumed[1] = dy
    } else {
      super.onNestedPreScroll(target, dx, dy, consumed)
    }
  }
  
  override fun onNestedScroll(target: View,
                              dxConsumed: Int, dyConsumed: Int,
                              dxUnconsumed: Int, dyUnconsumed: Int) {
    super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    var appBarLayout: AppBarLayout? = null
    var scrollTop = false
    var scrollEnd = false
    for (i in 0 until childCount) {
      val view = getChildAt(i)
      if (view is AppBarLayout) {
        appBarLayout = view
        continue
      }
      if (view is RecyclerView) {
        scrollTop = !view.canScrollVertically(-1)
        scrollEnd = !view.canScrollVertically(1)
      }
    }
  
    if (appBarLayout == null
      || (scrollTop && dyUnconsumed < 0 && isAppBarExpanded(appBarLayout))
      || (scrollEnd && dyUnconsumed > 0 && isAppBarCollapsed(appBarLayout))) {
      translationY(-dyUnconsumed.toFloat())
    }
  }
  
  override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
    return y.toInt() != originalRect.top || super.onNestedPreFling(target, velocityX, velocityY)
  }
  
  override fun onStopNestedScroll(target: View) {
    super.onStopNestedScroll(target)
    if (abs(y) > overScrollThreshold) {
      val yTranslation: Float = if (originalRect.top + y > 0) originalRect.height().toFloat() else -originalRect.height().toFloat()
      animate()
        .setDuration(200)
        .setInterpolator(AccelerateDecelerateInterpolator())
        .alpha(0f)
        .translationY(yTranslation)
        .setListener(object : Animator.AnimatorListener {
          override fun onAnimationStart(animation: Animator) {}
        
          override fun onAnimationEnd(animation: Animator) {
            if (overScrollListener != null) {
              (overScrollListener as OnOverScrollListener).onOverScroll()
            }
          }
        
          override fun onAnimationCancel(animation: Animator) {}
        
          override fun onAnimationRepeat(animation: Animator) {}
        })
      return
    }
  
    if (y.toInt() != originalRect.top) {
      animate()
        .setDuration(RESTORE_ANIM_DURATION.toLong())
        .setInterpolator(AccelerateDecelerateInterpolator())
        .translationY(originalRect.top.toFloat())
        .scaleX(1f)
        .scaleY(1f)
    }
  }
  
  private fun translationY(dy: Float) {
    y += dy * 0.5f
  }
  
  private fun isAppBarExpanded(appBarLayout: AppBarLayout): Boolean {
    return appBarLayout.top == 0
  }
  
  private fun isAppBarCollapsed(appBarLayout: AppBarLayout): Boolean = appBarLayout.y.toInt() == -appBarLayout.totalScrollRange
  
  interface OnOverScrollListener {
    fun onOverScroll()
  }
}