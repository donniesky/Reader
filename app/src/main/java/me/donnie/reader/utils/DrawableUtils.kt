package me.donnie.reader.utils

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.PaintDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.view.Gravity
import kotlin.math.max
import kotlin.math.roundToInt

object DrawableUtils {
  
  fun constrain(value: Float, min: Float, max: Float): Float {
    return if (value < min) min else if (value > max) max else value
  }
  
  fun makeScrimDrawable(gravity: Int): Drawable? {
    return makeScrimDrawable(Color.BLACK, 9, gravity)
  }
  
  fun makeScrimDrawable(): Drawable? {
    return makeScrimDrawable(Gravity.BOTTOM)
  }
  
  fun makeScrimDrawable(baseColor: Int, numStops: Int, gravity: Int): Drawable? {
    var numStops = numStops
    numStops = max(numStops, 2)
    val paintDrawable = PaintDrawable()
    paintDrawable.shape = RectShape()
    val stopColors = IntArray(numStops)
    val red = Color.red(baseColor)
    val green = Color.green(baseColor)
    val blue = Color.blue(baseColor)
    val alpha = Color.alpha(baseColor)
    for (i in 0 until numStops) {
      val x = i * 1f / (numStops - 1)
      val opacity: Float = constrain(Math.pow(x.toDouble(), 3.0).toFloat(), 0f, 1f)
      stopColors[i] = Color.argb((alpha * opacity).toInt(), red, green, blue)
    }
    val x0: Float
    val x1: Float
    val y0: Float
    val y1: Float
    when (gravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
      Gravity.LEFT -> {
        x0 = 1f
        x1 = 0f
      }
      Gravity.RIGHT -> {
        x0 = 0f
        x1 = 1f
      }
      else -> {
        x0 = 0f
        x1 = 0f
      }
    }
    when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
      Gravity.TOP -> {
        y0 = 1f
        y1 = 0f
      }
      Gravity.BOTTOM -> {
        y0 = 0f
        y1 = 1f
      }
      else -> {
        y0 = 0f
        y1 = 0f
      }
    }
    paintDrawable.shaderFactory = object : ShapeDrawable.ShaderFactory() {
      override fun resize(width: Int, height: Int): Shader {
        return LinearGradient(
          width * x0,
          height * y0,
          width * x1,
          height * y1,
          stopColors, null,
          Shader.TileMode.CLAMP)
      }
    }
    paintDrawable.alpha = (0.4f * 255).roundToInt()
    return paintDrawable
  }
  
}