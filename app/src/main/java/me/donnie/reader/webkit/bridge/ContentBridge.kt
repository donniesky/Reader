package me.donnie.reader.webkit.bridge

import android.webkit.JavascriptInterface
import timber.log.Timber

open class ContentBridge(
  private val delegate: ContentBridgeDelegate? = null
) : BaseBridge(delegate) {
  
  interface ContentBridgeDelegate : BaseBridgeDelegate {
    fun onDocumentReady()
    fun onTextChanged(length: Int)
    fun provideContentMinHeight(): Int
    fun providePlaceholder(): String?
  }
  
  @JavascriptInterface
  fun showHtml(html: String) {
    Timber.tag("ContentBridge").d("showHtml: $html")
  }
  
  @JavascriptInterface
  fun providePlaceholder(): String {
    if (delegate == null) return ""
    val placeholder = delegate.providePlaceholder()
    if (!placeholder.isNullOrEmpty()) {
      return placeholder
    }
    return ""
  }
  
  @JavascriptInterface
  fun provideContentMinHeight(): Int {
    if (delegate == null) {
      return 0
    }
    return delegate.provideContentMinHeight()
  }
  
  @JavascriptInterface
  fun onDocumentReady() {
    postCallback(Runnable {
      delegate?.onDocumentReady()
    })
  }
  
  @JavascriptInterface
  fun onTextChanged(length: Int) {
    postCallback(Runnable {
      delegate?.onTextChanged(length)
    })
  }
  
  fun callRequestContentFocus() {
    runJavaScript("requestContentFocus", "")
  }
  
  fun callSetPlaceholder(holder: String) {
    runJavaScript("setPlaceholder", holder)
  }
  
  fun callSetContentMinHeight(height: Int) {
    runJavaScript("setContentMinHeight", "$height")
  }
  
  fun callSetContentVisible(visible: Boolean) {
    runJavaScript("setContentVisible", "$visible")
  }
}