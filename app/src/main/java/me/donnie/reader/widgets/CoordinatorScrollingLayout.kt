package me.donnie.reader.widgets

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ScrollingView
import com.google.android.material.appbar.AppBarLayout
import me.donnie.reader.utils.setMargin
import timber.log.Timber

class CoordinatorScrollingLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
  CoordinatorLayout.AttachedBehavior {

  private var lastInsets: WindowInsets? = null
  
  init {
    fitsSystemWindows = true
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        systemUiVisibility = (systemUiVisibility
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
      }
    }
  }

  override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
    lastInsets = insets
    return insets
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    if (lastInsets != null) {
      val scrollingView = findScrollingView()
      val scrollingChildView = if (scrollingView != null) findChildView(scrollingView) else null
      for (i in 0 until childCount) {
        val childView = getChildAt(i)
        if (childView !== scrollingChildView) {
          childView.setMargin(
            lastInsets!!.systemWindowInsetLeft,
            0, lastInsets!!.systemWindowInsetRight,
            lastInsets!!.systemWindowInsetBottom
          )
        }
      }
      if (scrollingView != null) {
        if (scrollingView.fitsSystemWindows) {
          scrollingView.onApplyWindowInsets(lastInsets)
        } else {
          scrollingView.setPadding(
            scrollingView.paddingLeft,
            scrollingView.paddingTop,
            scrollingView.paddingRight,
            lastInsets!!.systemWindowInsetBottom
          )
        }
      }
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
  }

  private fun findChildView(view: View?): View? {
    var targetView = view
    while (targetView != null) {
      val parent = targetView.parent
      if (parent === this) {
        return targetView
      }
      targetView = parent as View
    }
    return null
  }

  private fun findScrollingView(): View? {
    return findScrollingView(this)
  }

  private fun findScrollingView(viewGroup: ViewGroup): View? {
    for (i in 0 until viewGroup.childCount) {
      Timber.tag("测试测试").d("findlistview: $childCount")
      val view = viewGroup.getChildAt(i)
      if (view is ScrollingView) {
        return view
      }
      if (view is ViewGroup) {
        val scrollingView = findScrollingView(view)
        if (scrollingView != null) {
          return scrollingView
        }
      }
    }
    return null
  }

  override fun getBehavior(): CoordinatorLayout.Behavior<*> {
    return Behavior()
  }

  private class Behavior : AppBarLayout.ScrollingViewBehavior() {
    override fun onMeasureChild(
      parent: CoordinatorLayout,
      child: View,
      parentWidthMeasureSpec: Int,
      widthUsed: Int,
      parentHeightMeasureSpec: Int,
      heightUsed: Int
    ): Boolean {
      var phMeasureSpec = parentHeightMeasureSpec
      val parentInsets = parent.lastWindowInsets
      if (parentInsets != null) {
        var parentHeightSize = MeasureSpec.getSize(phMeasureSpec)
        parentHeightSize -= parentInsets.systemWindowInsetTop
        val parentHeightMode = MeasureSpec.getMode(phMeasureSpec)
        phMeasureSpec = MeasureSpec.makeMeasureSpec(parentHeightSize, parentHeightMode)
      }
      return super.onMeasureChild(
        parent, child, parentWidthMeasureSpec,
        widthUsed, phMeasureSpec, heightUsed
      )
    }
  }
}