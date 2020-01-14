package me.donnie.reader.webkit.bridge

import android.text.TextUtils
import android.webkit.WebView

open class BaseBridge(
  private val delegate: BaseBridgeDelegate?
) {
  
  interface BaseBridgeDelegate {
    fun provideWebView(): WebView?
  }
  
  fun postCallback(runnable: Runnable) {
    delegate?.provideWebView()?.post(runnable)
  }
  
  fun runJavaScript(function: String, vararg params: String) {
    val webView = delegate?.provideWebView()
    webView?.post {
      webView.loadUrl("javascript:$function(('${TextUtils.join("','", params)}'));")
    }
  }
  
  fun destroy() {
  
  }
}