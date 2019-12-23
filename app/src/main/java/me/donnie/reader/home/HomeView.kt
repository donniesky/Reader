package me.donnie.reader.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import me.donnie.reader.R
import me.donnie.reader.data.entities.Article
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
  
  fun bindArticle(article: Article) {
    title.text = article.title
    image.loadImage(article.image)
    time.text = article.date
  }
  
  fun unbind() {
    image.setImageDrawable(null)
  }
  
  companion object {
    
    @JvmStatic
    fun newCardItem(context: Context): View {
      return CardView(context).apply {
        layoutParams = LayoutParams(matchParent, wrapContent).apply {
          topMargin = 5.dp
          bottomMargin = 5.dp
        }
        radius = 5.dp.toFloat()
        elevation = 10.dp.toFloat()
        addView(HomeView(context), frameLayoutParams(matchParent, wrapContent))
      }
    }
  }
}