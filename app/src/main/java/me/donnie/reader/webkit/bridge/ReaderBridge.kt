package me.donnie.reader.webkit.bridge

import android.text.TextUtils
import android.webkit.JavascriptInterface

class ReaderBridge(
  private val delegate: ReaderBridgeDelegate?
) : BaseBridge(delegate) {
  
  interface ReaderBridgeDelegate : BaseBridgeDelegate {
    fun onClickBody()
    fun onWindowLoad()
    fun provideContent(): String
    fun provideContentType(): Int
    fun provideBodyFontSize(): Int
    fun provideTitleFontSize(): Int
  }
  
  @JavascriptInterface
  fun onClickBody() {
    postCallback(Runnable { delegate?.onClickBody() })
  }
  
  @JavascriptInterface
  fun onWindowLoad() {
    postCallback(Runnable { delegate?.onWindowLoad() })
  }
  
  @JavascriptInterface
  fun isThemeDark(): Boolean = false
  
  @JavascriptInterface
  fun provideUserId(): String = ""
  
  @JavascriptInterface
  fun provideContent(): String {
    if (delegate == null) return ""
    
    val content = delegate.provideContent()
    if (!TextUtils.isEmpty(content)) {
      return content
    }
    return ""
  }
  
  @JavascriptInterface
  fun provideContentType(): Int {
    if (delegate != null) {
      return delegate.provideContentType()
    }
    return 4
  }
  
  @JavascriptInterface
  fun provideBodyFontSize(): Int {
    if (delegate != null) {
      return delegate.provideBodyFontSize()
    }
    return 20
  }
  
  @JavascriptInterface
  fun provideTitleFontSize(): Int {
    if (delegate != null) {
      return delegate.provideTitleFontSize()
    }
    return 24
  }
  
  public fun callSetBodyFontSize(size: Int) {
    runJavaScript("setBodyFontSize", size.toString())
  }
  
  public fun callSetTitleFontSize(size: Int) {
    runJavaScript("setTitleFontSize", size.toString());
  }
  
  public fun callSetContentMinHeight(size: Int) {
    runJavaScript("setContentMinHeight", size.toString())
  }
  
  public fun callSetContentOffset(offset: Int) {
    runJavaScript("setContentOffset", offset.toString())
  }
  
  public fun callSetContentVisible(visible: Boolean) {
    runJavaScript("setContentVisible", "$visible")
  }
  
  public fun callSetupTheme() {
    runJavaScript("setupTheme", "")
  }
  
}