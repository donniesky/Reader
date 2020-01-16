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
      if (resources.configuration.fontScale == 1.0f) {
        layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
      }
      databaseEnabled = true
      domStorageEnabled = true
      saveFormData = true
      setAppCacheEnabled(true)
      setAppCachePath(context.cacheDir.toString())
      cacheMode = LOAD_DEFAULT
      mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
      defaultTextEncodingName = "UTF-8"
      useWideViewPort = false
      builtInZoomControls = false
      displayZoomControls = false
      javaScriptEnabled = true
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