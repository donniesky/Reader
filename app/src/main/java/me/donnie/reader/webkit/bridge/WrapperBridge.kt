package me.donnie.reader.webkit.bridge

import android.webkit.JavascriptInterface

class WrapperBridge(
  private val delegate: WrapperBridgeDelegate? = null
) : BaseBridge(delegate) {
  
  interface WrapperBridgeDelegate : BaseBridgeDelegate {
    fun provideContentPaddingBottom(): Int
    fun provideContentPaddingLeft(): Int
    fun provideContentPaddingRight(): Int
    fun provideContentPaddingTop(): Int
  }
  
  @JavascriptInterface
  fun provideContentPaddingTop(): Int {
    if (delegate != null) {
      return delegate.provideContentPaddingTop()
    }
    return 0
  }
  
  @JavascriptInterface
  fun provideContentPaddingBottom(): Int {
    if (delegate != null) {
      return delegate.provideContentPaddingBottom()
    }
    return 0
  }
  
  @JavascriptInterface
  fun provideContentPaddingLeft(): Int {
    if (delegate != null) {
      return delegate.provideContentPaddingLeft()
    }
    return 0
  }
  
  @JavascriptInterface
  fun provideContentPaddingRight(): Int {
    if (delegate != null) {
      return delegate.provideContentPaddingRight()
    }
    return 0
  }
  
  fun callSetContentPaddingTop(paddingTop: Int) {
    runJavaScript("setContentPaddingTop", "$paddingTop")
  }
  
  fun callSetContentPaddingRight(paddingRight: Int) {
    runJavaScript("setContentPaddingRight", "$paddingRight")
  }
  
  fun callSetContentPaddingBottom(paddingBottom: Int) {
    runJavaScript("setContentPaddingBottom", "$paddingBottom")
  }
  
  fun callSetContentPaddingLeft(paddingLeft: Int) {
    runJavaScript("setContentPaddingLeft", "$paddingLeft")
  }
  
  fun callSetContentPadding(
    paddingTop: Int, paddingRight: Int,
    paddingBottom: Int, paddingLeft: Int
  ) {
    runJavaScript(
      "setContentPadding",
      "$paddingTop", "$paddingRight",
      "$paddingBottom", "$paddingLeft"
    )
  }
  
}