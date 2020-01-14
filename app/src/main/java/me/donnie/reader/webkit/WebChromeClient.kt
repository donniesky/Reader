package me.donnie.reader.webkit

import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView

class WebChromeClient(
  private val listener: Listener? = null
) : WebChromeClient() {
  
  interface Listener {
    fun isPageFinishing(): Boolean
  }
  
  override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
    return listener?.isPageFinishing() == true || super.onJsAlert(view, url, message, result)
  }
  
  override fun onJsConfirm(
    view: WebView?,
    url: String?,
    message: String?,
    result: JsResult?
  ): Boolean {
    return listener?.isPageFinishing() == true || super.onJsConfirm(view, url, message, result)
  }
  
  override fun onJsPrompt(
    view: WebView?,
    url: String?,
    message: String?,
    defaultValue: String?,
    result: JsPromptResult?
  ): Boolean {
    return listener?.isPageFinishing() == true || super.onJsPrompt(view, url, message, defaultValue, result)
  }
  
  override fun onJsBeforeUnload(
    view: WebView?,
    url: String?,
    message: String?,
    result: JsResult?
  ): Boolean {
    return listener?.isPageFinishing() == true || super.onJsBeforeUnload(view, url, message, result)
  }
  
  override fun onProgressChanged(view: WebView, newProgress: Int) {
  }
  
  override fun onReceivedTitle(view: WebView, title: String?) {
  
  }
  
}