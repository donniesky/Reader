package me.donnie.reader.data

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jaxb.JaxbConverterFactory

object ReaderClient {
  
  // https://criticalgnome.com/en/2018/10/29/two-converters-in-one-retrofit-project/
  
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
      .addConverterFactory(JaxbConverterFactory.create())
      .build().create(service)
  }
  
}