@file:Suppress("DEPRECATION")

package me.donnie.reader.utils

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import me.donnie.reader.data.Json
import me.donnie.reader.data.Xml
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.lang.reflect.Type

class XmlOrJsonConverterFactory() : Converter.Factory() {
  
  override fun responseBodyConverter(type: Type,
                                     annotations: Array<Annotation>,
                                     retrofit: Retrofit): Converter<ResponseBody, *>? {
    annotations.forEach { annotation ->
      when (annotation) {
        is Xml -> return SimpleXmlConverterFactory.create().responseBodyConverter(type, annotations, retrofit)
        is Json -> return GsonConverterFactory.create(createGson()).responseBodyConverter(type, annotations, retrofit)
      }
    }
    return GsonConverterFactory.create().responseBodyConverter(type, annotations, retrofit)
  }
  
  companion object {
    @JvmStatic
    fun create() = XmlOrJsonConverterFactory()
  }
  
  private fun createGson(): Gson {
    return GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .setDateFormat("yyyy-MM-dd HH:mm:ss")
      .setPrettyPrinting()
      .create()
  }
}