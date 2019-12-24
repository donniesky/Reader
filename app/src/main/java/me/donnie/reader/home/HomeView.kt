package me.donnie.reader.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import me.donnie.reader.R
import me.donnie.reader.data.entities.Item
import me.donnie.reader.utils.*

class HomeView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
  
  val title = TextView(context).apply {
    id = R.id.title
  }
  
  val image = ImageView(context).apply {
    id = R.id.image
    scaleType = ImageView.ScaleType.CENTER_CROP
  }
  
  val time = TextView(context).apply {
    id = R.id.time
  }
  
  init {
    id = R.id.home
    layoutParams = LayoutParams(matchParent, wrapContent)
    
    addView(image, constraintLayoutParams(120.dp, matchConstraint) {
      topToTop = parentId
      endToEnd = parentId
      bottomToBottom = parentId
    })
    
    addView(title, constraintLayoutParams(matchConstraint, wrapContent) {
      startToStart = parentId
      topToTop = parentId
      endToStartOf(image)
      marginStart = 8.dp
      topMargin = 8.dp
      marginEnd = 8.dp
    })
    
    addView(time, constraintLayoutParams(wrapContent, wrapContent) {
      startToStartOf(title)
      topToBottomOf(title)
      bottomToBottom = parentId
      topMargin = 16.dp
      bottomMargin = 8.dp
    })
  }
  
  fun bindItem(item: Item) {
    title.text = item.title
    image.loadImage(item.image?.url)
    time.text = item.pubDate
  }
  
  fun unbind() {
    image.setImageDrawable(null)
  }
  
  companion object {
    
    @JvmStatic
    fun newCardItem(context: Context): View {
      return CardView(context).apply {
        layoutParams = LayoutParams(matchParent, wrapContent).apply {
          topMargin = 8.dp
          bottomMargin = 8.dp
        }
        radius = 3.dp.toFloat()
        elevation = 3.dp.toFloat()
        addView(HomeView(context), frameLayoutParams(matchParent, wrapContent))
      }
    }
  }
}