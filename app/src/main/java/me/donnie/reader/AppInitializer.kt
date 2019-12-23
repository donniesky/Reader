package me.donnie.reader

import me.donnie.reader.utils.InitProvider
import timber.log.Timber

class AppInitializer : InitProvider() {
  override fun onCreate(): Boolean {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
    return true
  }
}