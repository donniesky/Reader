package me.donnie.reader.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProviders
import androidx.webkit.WebViewClientCompat
import kotlinx.android.synthetic.main.activity_news_detail.*
import me.donnie.reader.R
import me.donnie.reader.data.entities.Result
import me.donnie.reader.utils.DrawableUtils
import me.donnie.reader.utils.loadImage
import me.donnie.reader.utils.observeNotNull
import me.donnie.reader.utils.setVisisble
import timber.log.Timber

class NewsDetailActivity : AppCompatActivity() {
  
  companion object {
    
    @JvmStatic
    fun newIntent(context: Context, uri: Uri): Intent {
      val intent = Intent(context, NewsDetailActivity::class.java).setData(uri)
      /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
          .addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
      }*/
      return intent
    }
    
    @JvmStatic
    fun newIntent(context: Context, url: String): Intent = newIntent(context, Uri.parse(url))
  }
  
  private lateinit var model: NewsDetailViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_news_detail)
    model = ViewModelProviders.of(this).get(NewsDetailViewModel::class.java)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(false)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    ViewCompat.setBackground(scrim, DrawableUtils.makeScrimDrawable())
    toolbar.setNavigationOnClickListener {
      onBackPressed()
    }
    setupWebView()
    model.result.observeNotNull(this) {
      if (it is Result.Success) {
        image.loadImage(it.data.img)
        title = it.data.title
      }
    }
    
    if (savedInstanceState == null) {
      model.parseNewsLink(intent.data.toString())
    }
  }
  
  @SuppressLint("SetJavaScriptEnabled")
  private fun setupWebView() {
    val settings = webview.settings.apply {
      builtInZoomControls = false
      displayZoomControls = false
      domStorageEnabled = true
      databaseEnabled = true
      loadWithOverviewMode = true
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
      }
      javaScriptEnabled = true
      useWideViewPort = false
    }
    val defaultUserAgent = webview.settings.userAgentString
    val newUserAgent = defaultUserAgent
      .replaceFirst("\\(Linux;.*?\\)", "(X11; Linux x86_64)")
      .replace("Mobile Safari/", "Safari/")
    settings.userAgentString = newUserAgent
    webview.webChromeClient = object : WebChromeClient() {
      override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        return isFinishing || super.onJsAlert(view, url, message, result)
      }
  
      override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
      ): Boolean {
        return isFinishing || super.onJsConfirm(view, url, message, result)
      }
  
      override fun onJsPrompt(
        view: WebView?,
        url: String?,
        message: String?,
        defaultValue: String?,
        result: JsPromptResult?
      ): Boolean {
        return isFinishing || super.onJsPrompt(view, url, message, defaultValue, result)
      }
  
      override fun onJsBeforeUnload(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
      ): Boolean {
        return isFinishing || super.onJsBeforeUnload(view, url, message, result)
      }
  
      override fun onProgressChanged(view: WebView, newProgress: Int) {
        progress.setVisisble(newProgress != 100)
      }
  
      override fun onReceivedTitle(view: WebView, title: String?) {
        if (!title.isNullOrEmpty()) {
          setTitle(title)
        }
      }
    }
    webview.webViewClient = object : WebViewClientCompat() {
      override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
      
      }
  
      override fun onPageFinished(view: WebView, url: String?) {
        view.loadUrl("javascript:addAllRuby(rubyObject,[])")
      }
  
      override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
      ) {
        desc.text = description
      }
  
      override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        Timber.d("${request.method} -- ${request.url} -- ${request.requestHeaders}")
        return true
      }
    }
    loadUri(webview)
  }
  
  private fun loadUri(webView: WebView) {
    val url = intent.data.toString()
    webView.loadUrl(url)
  }
  
  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    webview.requestLayout()
  }
  
  override fun onBackPressed() {
    if (webview.canGoBack()) {
      webview.goBack()
    } else {
      super.onBackPressed()
    }
  }
}