package me.donnie.reader.webkit

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebSettings.LOAD_DEFAULT
import android.webkit.WebView
import me.donnie.reader.webkit.bridge.BaseBridge

@SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
open class BridgeView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyle: Int = 0
) : BaseWebView(context, attrs, defStyle) {
  
  private val bridges = mutableListOf<BaseBridge>()
  
  var isNeedDestroy = false
  
  init {
    webViewClient = WebViewClient(true)
    webChromeClient = WebChromeClient()
    isHorizontalScrollBarEnabled = false
    settings.apply {
      layoutAlgorithm = if (resources.configuration.fontScale != 1.0f) {
        WebSettings.LayoutAlgorithm.NARROW_COLUMNS
      } else {
        WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
      }
      setAppCacheEnabled(true)
      setAppCachePath(context.cacheDir.toString())
      cacheMode = LOAD_DEFAULT
      defaultTextEncodingName = "UTF-8"
      displayZoomControls = false
      javaScriptEnabled = true
      WebView.setWebContentsDebuggingEnabled(false)
    }
  }
  
  fun addBridge(bridge: BaseBridge?) {
    if (bridge != null && !bridges.contains(bridge)) {
      bridges.add(bridge)
      addJavascriptInterface(bridge, bridge.javaClass.simpleName)
    }
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    if (isNeedDestroy) {
      bridges.forEach {
        removeJavascriptInterface(it.javaClass.simpleName)
        it.destroy()
      }
    }
  }
}