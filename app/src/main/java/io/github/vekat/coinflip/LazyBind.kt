package io.github.vekat.coinflip

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
  return unsafeLazy { findViewById<T>(idRes) }
}

fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
  return unsafeLazy { findViewById<T>(idRes) }
}

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)