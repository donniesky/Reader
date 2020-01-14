package me.donnie.reader.utils

import java.io.*
import java.nio.charset.Charset

object StreamUtils {
  
  @JvmStatic
  fun parse(inputStream: InputStream): String {
    return parse(inputStream, Charset.forName("UTF-8"))
  }
  
  @JvmStatic
  fun parse(inputStream: InputStream, charset: Charset): String {
    return parse(InputStreamReader(inputStream, charset))
  }
  
  @JvmStatic
  fun parse(reader: Reader): String {
    try {
      val sw = StringWriter()
      val cArr = CharArray(8192)
      while (true) {
        val read = reader.read(cArr)
        if (read == -1) {
          return sw.toString()
        }
        sw.write(cArr, 0, read)
      }
    } finally {
      close(reader)
    }
  }
  
  fun close(closeable: Closeable?) {
    if (closeable != null) {
      try {
        closeable.close()
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }
}