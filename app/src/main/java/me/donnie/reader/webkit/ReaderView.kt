package me.donnie.reader.webkit

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import me.donnie.reader.utils.StreamUtils
import me.donnie.reader.webkit.bridge.ContentBridge
import me.donnie.reader.webkit.bridge.ImageBridge
import me.donnie.reader.webkit.bridge.ReaderBridge
import me.donnie.reader.webkit.bridge.WrapperBridge
import org.jsoup.Jsoup

class ReaderView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyle: Int = 0
) : BridgeView(context, attrs, defStyle),
  ReaderBridge.ReaderBridgeDelegate,
  ContentBridge.ContentBridgeDelegate,
  WrapperBridge.WrapperBridgeDelegate {
  
  var listener: Listener? = null
  
  private val disposable = CompositeDisposable()
  
  private var content = ""
  
  private val readerBridge = ReaderBridge(this)
  private val wrapperBridge = WrapperBridge(this)
  
  private var contentPaddingTop = 0
  private var contentPaddingBottom = 0
  private var contentPaddingLeft = 0
  private var contentPaddingRight = 0
  
  interface Listener {
    fun onActionModeStart()
    fun onActionModeDestroy()
    fun onClickBody()
    fun onDocumentReady()
  }
  
  init {
    val contentBridge = ContentBridge(this)
    val imageBridge = ImageBridge(this)
    addBridge(readerBridge)
    addBridge(wrapperBridge)
    addBridge(contentBridge)
    addBridge(imageBridge)
  }
  
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    listener = null
    disposable.clear()
  }
  
  override fun getContentHeight(): Int {
    return computeVerticalScrollRange()
  }
  
  override fun startActionMode(callback: ActionMode.Callback): ActionMode {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
      listener?.onActionModeStart()
    }
    return super.startActionMode(object : ActionMode.Callback {
      override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return callback.onActionItemClicked(mode, item)
      }
      
      override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return callback.onCreateActionMode(mode, menu)
      }
      
      override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return callback.onPrepareActionMode(mode, menu)
      }
      
      override fun onDestroyActionMode(mode: ActionMode?) {
        callback.onDestroyActionMode(mode)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
          listener?.onActionModeDestroy()
        }
      }
      
    })
  }
  
  @TargetApi(Build.VERSION_CODES.M)
  override fun startActionMode(callback: ActionMode.Callback, type: Int): ActionMode {
    listener?.onActionModeStart()
    return super.startActionMode(object : ActionMode.Callback2() {
      override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return callback.onActionItemClicked(mode, item)
      }
      
      override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return callback.onCreateActionMode(mode, menu)
      }
      
      override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return callback.onPrepareActionMode(mode, menu)
      }
      
      override fun onDestroyActionMode(mode: ActionMode?) {
        callback.onDestroyActionMode(mode)
        listener?.onActionModeDestroy()
      }
      
      override fun onGetContentRect(mode: ActionMode?, view: View?, outRect: Rect?) {
        if (callback is ActionMode.Callback2) {
          callback.onGetContentRect(mode, view, outRect)
        } else {
          super.onGetContentRect(mode, view, outRect)
        }
      }
    }, type)
  }
  
  fun setContent(content: String) {
    loadContent(content)
  }
  
  private fun loadContent(content: String, type: Int = 0) {
    Observable.create(ObservableOnSubscribe<String> { emitter ->
      this@ReaderView.content = content
      emitter.onNext("4")
      emitter.onComplete()
    }).subscribeOn(Schedulers.newThread())
      .observeOn(Schedulers.io())
      .lift(ObservableOperator<String, String> { observer ->
        object : Observer<String> {
          override fun onComplete() {
            observer.onComplete()
          }
          
          override fun onSubscribe(d: Disposable) {
          
          }
          
          override fun onNext(t: String) {
            try {
              Jsoup.parse(context.assets.open("webview/html/reader.html"), null, "")
              observer.onNext(StreamUtils.parse(context.assets.open("webview/html/reader.html")))
            } catch (e: Exception) {
              observer.onError(e)
            }
          }
          
          override fun onError(e: Throwable) {
            observer.onError(e)
          }
        }
      }).observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        loadDataWithBaseURL("", it, "text/html", "utf-8", null)
      }, {
        it.printStackTrace()
      }).addTo(disposable)
  }
  
  override fun onClickBody() {
    listener?.onClickBody()
  }
  
  override fun onWindowLoad() {
  
  }
  
  override fun provideContent(): String = content
  
  override fun provideContentType(): Int = 4
  
  override fun provideBodyFontSize(): Int = 20
  
  override fun provideTitleFontSize(): Int = 24
  
  override fun onDocumentReady() {
    listener?.onDocumentReady()
  }
  
  override fun onTextChanged(length: Int) {
  }
  
  override fun provideContentMinHeight(): Int = 0
  
  override fun providePlaceholder(): String? = null
  
  fun setContentPaddingTop(padding: Int) {
    contentPaddingTop = padding
    wrapperBridge.callSetContentPaddingTop(padding)
  }
  
  fun setContentPaddingBottom(padding: Int) {
    contentPaddingBottom = padding
    wrapperBridge.callSetContentPaddingBottom(padding)
  }
  
  fun setContentPaddingLeft(padding: Int) {
    contentPaddingLeft = padding
    wrapperBridge.callSetContentPaddingLeft(padding)
  }
  
  fun setContentPaddingRight(padding: Int) {
    contentPaddingRight = padding
    wrapperBridge.callSetContentPaddingRight(padding)
  }
  
  override fun provideContentPaddingBottom(): Int = contentPaddingBottom
  
  override fun provideContentPaddingLeft(): Int = contentPaddingLeft
  
  override fun provideContentPaddingRight(): Int = contentPaddingRight
  
  override fun provideContentPaddingTop(): Int = contentPaddingTop
  
  override fun provideWebView(): WebView? = this
  
  
}