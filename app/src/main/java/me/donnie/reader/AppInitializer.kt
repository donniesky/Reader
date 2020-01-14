package me.donnie.reader

import com.facebook.drawee.backends.pipeline.Fresco
import me.donnie.reader.utils.InitProvider
import me.donnie.reader.utils.injectAsAppCtx
import timber.log.Timber

class AppInitializer : InitProvider() {
  override fun onCreate(): Boolean {
    context!!.injectAsAppCtx()
    Fresco.initialize(context)
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
    return true
  }
}