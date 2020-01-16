package me.donnie.reader.webkit.bridge

import android.net.Uri
import android.webkit.JavascriptInterface
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.datasource.BaseDataSubscriber
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.common.Priority
import com.facebook.imagepipeline.request.ImageRequest
import me.donnie.reader.utils.FrescoUtils
import org.json.JSONArray
import org.json.JSONException
import timber.log.Timber

open class ImageBridge(
  private val delegate: BaseBridgeDelegate? = null
) : BaseBridge(delegate) {

  @JavascriptInterface
  fun readImageCache(url: String): String? {
    val imageCache = FrescoUtils.getLocalImageCache(url, delegate?.provideWebView()) ?: return null
    return Uri.fromFile(imageCache).toString()
  }

  @JavascriptInterface
  fun isAutoLoadImage(): Boolean {
    return delegate != null
  }
  
  @JavascriptInterface
  fun loadImage(url: String) {
    Timber.tag("ImageBridge").d("loadImage url: $url")
    if (delegate != null) {
      Fresco.getImagePipeline().prefetchToDiskCache(
        ImageRequest.fromUri(url),
        delegate.provideWebView(),
        Priority.HIGH).subscribe(object : BaseDataSubscriber<Void>() {
        override fun onFailureImpl(dataSource: DataSource<Void>?) {
          callOnImageLoadFailed(url)
        }
  
        override fun onNewResultImpl(dataSource: DataSource<Void>) {
          val imageCache = FrescoUtils.getLocalImageCache(url, delegate.provideWebView())
          /*var i = 0
          while (true) {
            if ((imageCache == null || !imageCache.exists()) && i < 5) {
              imageCache = FrescoUtils.getLocalImageCache(url, delegate.provideWebView())
              i++
              try {
                Thread.sleep(100)
              } catch (e: InterruptedException) {
                e.printStackTrace()
              }
            }
          }*/
          if (imageCache == null || !imageCache.exists()) {
            callOnImageLoadFailed(url)
          } else {
            callOnImageLoadSuccess(url, Uri.fromFile(imageCache).toString())
          }
        }
      }, CallerThreadExecutor.getInstance())
    }
  }
  
  @JavascriptInterface
  fun openImage(urls: String, index: Int) {
    if (delegate != null) {
      val list = mutableListOf<String>()
      try {
        val jsonArray = JSONArray(urls)
        for (i in 0 until jsonArray.length()) {
          list.add(jsonArray.get(i).toString())
        }
        postCallback(Runnable {
          // open image activity
        })
      } catch (e: JSONException) {
        e.printStackTrace()
      }
    }
  }
  
  fun callOnImageLoadFailed(url: String) {
    runJavaScript("onImageLoadFailed", url)
  }
  
  fun callOnImageLoadSuccess(url: String, localUrl: String) {
    runJavaScript("onImageLoadSuccess", url, localUrl)
  }
}