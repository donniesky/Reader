package me.donnie.reader.utils

import me.donnie.reader.data.Json
import me.donnie.reader.data.Xml
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.jaxb.JaxbConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type

class XmlOrJsonConverterFactory : Converter.Factory() {
  
  override fun responseBodyConverter(type: Type,
                                     annotations: Array<Annotation>,
                                     retrofit: Retrofit): Converter<ResponseBody, *>? {
    annotations.forEach { annotation ->
      when (annotation) {
        is Xml -> return JaxbConverterFactory.create().responseBodyConverter(type, annotations, retrofit)
        is Json -> return MoshiConverterFactory.create().responseBodyConverter(type, annotations, retrofit)
      }
    }
    return MoshiConverterFactory.create().responseBodyConverter(type, annotations, retrofit)
  }
  
  companion object {
    @JvmStatic
    fun create() = XmlOrJsonConverterFactory()
  }
}