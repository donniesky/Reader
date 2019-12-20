package me.donnie.reader.utils

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction

inline fun FragmentActivity.fragmentTransaction(
  now: Boolean = true,
  allowStateLoss: Boolean = false,
  transactionBody: FragmentTransaction.() -> Unit
): Unit = supportFragmentManager.beginTransaction()
  .apply(transactionBody).let {
    if (allowStateLoss) {
      if (now) it.commitNowAllowingStateLoss() else it.commitAllowingStateLoss()
    } else {
      if (now) it.commitNow() else it.commit()
    }
  }

inline fun Fragment.fragmentTransaction(
  now: Boolean = true,
  allowStateLoss: Boolean = false,
  transactionBody: FragmentTransaction.() -> Unit
): Unit = childFragmentManager.beginTransaction()
  .apply(transactionBody).let {
    if (allowStateLoss) {
      if (now) it.commitNowAllowingStateLoss() else it.commitAllowingStateLoss()
    } else {
      if (now) it.commitNow() else it.commit()
    }
  }

@Suppress("NOTHING_TO_INLINE")
inline fun FragmentTransaction.addToBackStack(): FragmentTransaction = addToBackStack(null)

inline fun <reified A : Activity> Fragment.start(configIntent: Intent.() -> Unit = {}) {
  startActivity(Intent(activity, A::class.java).apply(configIntent))
}

inline fun Fragment.startActivity(action: String, configIntent: Intent.() -> Unit = {}) {
  startActivity(Intent(action).apply(configIntent))
}