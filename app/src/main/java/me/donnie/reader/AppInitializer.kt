package me.donnie.reader

import me.donnie.reader.utils.InitProvider
import me.donnie.reader.utils.injectAsAppCtx
import timber.log.Timber

class AppInitializer : InitProvider() {
  override fun onCreate(): Boolean {
    context!!.injectAsAppCtx()
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
    return true
  }
}