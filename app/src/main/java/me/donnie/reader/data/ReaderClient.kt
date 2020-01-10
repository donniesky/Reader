package me.donnie.reader.data

import me.donnie.reader.BuildConfig
import me.donnie.reader.data.entities.*
import me.donnie.reader.utils.XmlOrJsonConverterFactory
import me.donnie.reader.utils.appCtx
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

object ReaderClient {
  
  private val reader = createReaderService()
  
  private val dict = createDictService()
  
  private fun createOkHttpClient(interceptors: Array<out Interceptor>): OkHttpClient.Builder {
    val builder = OkHttpClient.Builder().apply {
      cache(Cache(File(appCtx.cacheDir, "reader_cache"), 10 * 1024 * 1024))
      connectTimeout(20, TimeUnit.SECONDS)
      readTimeout(20, TimeUnit.SECONDS)
      writeTimeout(20, TimeUnit.SECONDS)
    }
    
    if (BuildConfig.DEBUG) {
      val httpLoggingInterceptor = HttpLoggingInterceptor(
        object : HttpLoggingInterceptor.Logger {
          override fun log(message: String) {
            Timber.tag("Reader").d(message)
          }
        }
      )
      builder.addNetworkInterceptor(httpLoggingInterceptor.apply {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
      })
    }
    interceptors.forEach {
      builder.addNetworkInterceptor(it)
    }
    return builder
  }
  
  private fun createReaderService(
    vararg interceptors: Interceptor
  ): ReaderService {
    return Retrofit.Builder()
      .baseUrl("https://reader.tiny4.org/")
      .client(createOkHttpClient(interceptors).build())
      .addConverterFactory(XmlOrJsonConverterFactory.create())
      .build().create(ReaderService::class.java)
  }
  
  private fun createDictService(vararg interceptors: Interceptor): DictService {
    return Retrofit.Builder()
      .baseUrl("http://www.cgdict.com/")
      .client(createOkHttpClient(interceptors).build())
      .addConverterFactory(GsonConverterFactory.create())
      .build().create(DictService::class.java)
  }
  
  suspend fun getNews(): Result<List<Item>> {
    return try {
      val news = reader.getNews("main", "1.0")
      val items = news.channel.item
      if (items != null && items.isNotEmpty()) {
        Result.Success(items)
      } else {
        Result.Failure()
      }
    } catch (e: Exception) {
      Timber.e(e)
      Result.Error(e)
    }
  }
  
  suspend fun getHotNews(): Result<List<Article>> {
    return try {
      val hotNews = reader.getHotNews()
      if (hotNews.data?.article != null &&
        hotNews.data.article.isNotEmpty()) {
        Result.Success(hotNews.data.article)
      } else {
        Result.Failure()
      }
    } catch (e: Exception) {
      Timber.e(e)
      Result.Error(e)
    }
  }
}

interface ReaderService {
  
  @Xml
  @GET("newsapi")
  suspend fun getNews(
    @Query("name") name: String,
    @Query("ver") version: String
  ): Rss
  
  @Json
  @GET("hots")
  suspend fun getHotNews(): HotsNews
}

interface DictService {

  @GET("index.php?app=api&ac=word&ts=search")
  suspend fun searchWord(@Query("word") word: String): Call<ResponseBody>
  
}