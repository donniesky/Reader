package me.donnie.reader.utils

import android.widget.ImageView
import coil.api.load
import me.donnie.reader.R

fun ImageView.loadImage(url: String?) {
  load(url) {
    crossfade(true)
    placeholder(R.color.colorPrimary)
  }
}