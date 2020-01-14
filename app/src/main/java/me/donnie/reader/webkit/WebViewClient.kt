package me.donnie.reader.webkit

import android.graphics.Bitmap
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.webkit.WebViewClientCompat

class WebViewClient(
  private val shouldInterceptRequest: Boolean = false
) : WebViewClientCompat() {
  
  override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
    super.onPageStarted(view, url, favicon)
  }
  
  override fun onPageFinished(view: WebView?, url: String?) {
    super.onPageFinished(view, url)
  }
  
  override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
    //TODO: navigate to another page.
    return true
  }
  
  override fun shouldInterceptRequest(
    view: WebView?,
    request: WebResourceRequest?
  ): WebResourceResponse? {
    if (!shouldInterceptRequest || !isImageUrl(request?.url.toString())) {
      return super.shouldInterceptRequest(view, request)
    }
    return WebResourceResponse("image/jpeg", "UTF-8", null)
  }

  private fun isImageUrl(url: String): Boolean {
    return url.startsWith("http") && (url.endsWith("jpg") ||
      url.endsWith("png") || url.endsWith("jpeg") ||
      url.endsWith("webp") || url.endsWith("gif"))
  }
}