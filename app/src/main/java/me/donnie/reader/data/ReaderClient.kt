package me.donnie.reader.data

import me.donnie.reader.utils.XmlOrJsonConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jaxb.JaxbConverterFactory
import retrofit2.http.GET

object ReaderClient {
  
  private fun createOkhttpClient(vararg interceptors: Interceptor): OkHttpClient.Builder {
    val builder = OkHttpClient.Builder()
    interceptors.forEach {
      builder.addNetworkInterceptor(it)
    }
    return builder
  }
  
  private fun <T> createReaderService(url: String,
                                  service: Class<T>): T {
    return Retrofit.Builder()
      .baseUrl("https://reader.tiny4.org/")
      .addConverterFactory(XmlOrJsonConverterFactory.create())
      .build().create(service)
  }
  
}

interface ReaderService {

  @GET("newsapi?name=main&ver=1.0")
  suspend fun getNews(): Rss
}