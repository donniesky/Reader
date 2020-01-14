package me.donnie.reader.webkit

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import com.github.ksoichiro.android.observablescrollview.ObservableWebView

open class BaseWebView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyle: Int = 0
) : ObservableWebView(context, attrs, defStyle) {
  
  fun runJavaScript(function: String, vararg params: String) {
    post {
      loadUrl("javascript:$function(('${TextUtils.join("','", params)}'));")
    }
  }
}