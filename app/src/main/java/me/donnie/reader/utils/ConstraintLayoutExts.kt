@file:Suppress("NOTHING_TO_INLINE", "unused")
package me.donnie.reader.utils

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

inline val ConstraintLayout.LayoutParams.parentId
  get() = ConstraintLayout.LayoutParams.PARENT_ID

inline val ConstraintLayout.LayoutParams.packed
  get() = ConstraintLayout.LayoutParams.CHAIN_PACKED

inline val ConstraintLayout.LayoutParams.spread
  get() = ConstraintLayout.LayoutParams.CHAIN_SPREAD

inline val ConstraintLayout.LayoutParams.spreadInside
  get() = ConstraintLayout.LayoutParams.CHAIN_SPREAD_INSIDE

inline val ConstraintLayout.matchConstraint
  get() = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT

inline fun ConstraintLayout.constraintLayoutParams(
  width: Int = matchConstraint,
  height: Int = matchConstraint,
  initParams: ConstraintLayout.LayoutParams.() -> Unit = {}
): ConstraintLayout.LayoutParams {
  return createConstraintLayoutParams(width, height)
    .apply(initParams).also { it.validate() }
}

@PublishedApi
internal fun ConstraintLayout.createConstraintLayoutParams(
  width: Int,
  height: Int
): ConstraintLayout.LayoutParams {
  val matchParentWidth = width == matchParent
  val matchParentHeight = height == matchParent
  val realWidth = if (matchParentWidth) matchConstraint else width
  val realHeight = if (matchParentHeight) matchConstraint else height
  return ConstraintLayout.LayoutParams(realWidth, realHeight).apply {
    if (matchParentWidth) centerHorizontally()
    if (matchParentHeight) centerVertically()
  }
}

inline fun ConstraintLayout.LayoutParams.centerHorizontally() {
  startToStart = parentId
  endToEnd = parentId
}

inline fun ConstraintLayout.LayoutParams.centerVertically() {
  topToTop = parentId
  bottomToBottom = parentId
}

inline fun ConstraintLayout.LayoutParams.centerInParent() {
  centerHorizontally()
  centerVertically()
}

inline fun ConstraintLayout.LayoutParams.topOfParent() {
  topToTop = parentId
}

inline fun ConstraintLayout.LayoutParams.bottomOfParent() {
  bottomToBottom = parentId
}

inline fun ConstraintLayout.LayoutParams.startOfParent() {
  startToStart = parentId
}

inline fun ConstraintLayout.LayoutParams.endOfParent() {
  endToEnd = parentId
}

inline fun ConstraintLayout.LayoutParams.leftOfParent() {
  leftToLeft = parentId
}

inline fun ConstraintLayout.LayoutParams.rightOfParent() {
  rightToRight = parentId
}

inline fun ConstraintLayout.LayoutParams.centerOn(view: View) {
  val id = view.existingOrNewId
  topToTop = id
  leftToLeft = id
  bottomToBottom = id
  rightToRight = id
}

inline fun ConstraintLayout.LayoutParams.topToTopOf(view: View) {
  topToTop = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.topToBottomOf(view: View) {
  topToBottom = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.bottomToTopOf(view: View) {
  bottomToTop = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.bottomToBottomOf(view: View) {
  bottomToBottom = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.baselineToBaselineOf(view: View) {
  baselineToBaseline = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.startToStartOf(view: View) {
  startToStart = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.startToEndOf(view: View) {
  startToEnd = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.endToStartOf(view: View) {
  endToStart = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.endToEndOf(view: View) {
  endToEnd = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.leftToLeftOf(view: View) {
  leftToLeft = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.leftToRightOf(view: View) {
  leftToRight = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.rightToRightOf(view: View) {
  rightToRight = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.rightToLeftOf(view: View) {
  rightToLeft = view.existingOrNewId
}

inline fun ConstraintLayout.LayoutParams.alignVerticallyOn(view: View) {
  val id = view.existingOrNewId
  topToTop = id
  bottomToBottom = id
}

inline fun ConstraintLayout.LayoutParams.alignHorizontallyOn(view: View) {
  val id = view.existingOrNewId
  leftToLeft = id
  rightToRight = id
}