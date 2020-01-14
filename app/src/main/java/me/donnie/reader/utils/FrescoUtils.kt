package me.donnie.reader.utils

import com.facebook.binaryresource.BinaryResource
import com.facebook.binaryresource.FileBinaryResource
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.request.ImageRequest
import java.io.File

object FrescoUtils {

  @JvmStatic
  fun getLocalImageCache(url: String, any: Any?): File? {
    val source: BinaryResource?
    if (url.isNotEmpty()) {
      val encodedCacheKey = DefaultCacheKeyFactory.getInstance()
        .getEncodedCacheKey(ImageRequest.fromUri(url), any)
      source = if (ImagePipelineFactory.getInstance()
          .mainFileCache.hasKey(encodedCacheKey)) {
        ImagePipelineFactory.getInstance()
          .mainFileCache.getResource(encodedCacheKey)
      } else {
        if (ImagePipelineFactory.getInstance()
            .smallImageFileCache.hasKey(encodedCacheKey)) {
          ImagePipelineFactory.getInstance()
            .smallImageFileCache.getResource(encodedCacheKey)
        } else {
          null
        }
      }
      if (source != null) {
        return (source as FileBinaryResource).file
      }
    }
    return null
  }

}