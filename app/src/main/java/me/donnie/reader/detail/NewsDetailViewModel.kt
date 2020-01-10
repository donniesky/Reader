package me.donnie.reader.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.donnie.reader.data.entities.NewsDetail
import me.donnie.reader.data.entities.Result
import me.donnie.reader.utils.appCtx
import org.jsoup.Jsoup
import timber.log.Timber

class NewsDetailViewModel : ViewModel() {
  
  private val _result = MutableLiveData<Result<NewsDetail>>()
  val result: LiveData<Result<NewsDetail>> = _result
  
  private fun parseLink(url: String): Result<NewsDetail> {
    val assetManager = appCtx.resources.assets
    return try {
      val inputStream = assetManager.open("webview/html/reader.html")
      val template = Jsoup.parse(inputStream, null, "")
      
      val document = Jsoup.connect(url).get()
      val title = document.title()
      val img = document.select("img").first()
      val imgSrc = img.absUrl("src")
  
      val content = template.getElementById("content")
      content.attr("style", "visibility: visible;")
      val container = document.getElementsByClass("container").html()
      val html = content.append(container).html()
      
      Timber.d("template html: ${template.html()} <><><><><><")
      inputStream.close()
      Result.Success(NewsDetail(title = title, img = imgSrc, content = container, html = template.html()))
    } catch (e: Exception) {
      e.printStackTrace()
      Result.Error(e)
    }
  }
  
  fun parseNewsLink(url: String) = viewModelScope.launch {
    _result.value = withContext(Dispatchers.IO) {
      parseLink(url)
    }
  }
  
}