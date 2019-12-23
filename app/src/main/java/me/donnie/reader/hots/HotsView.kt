package me.donnie.reader.hots

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import me.donnie.reader.R
import me.donnie.reader.data.entities.Article
import me.donnie.reader.home.HomeView
import me.donnie.reader.utils.*

class HotsView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
  
  val image = ImageView(context).apply {
    id = R.id.image
    scaleType = ImageView.ScaleType.CENTER_CROP
  }
  
  val title = TextView(context).apply {
    id = R.id.title
  }
  
  val time = TextView(context).apply {
    id = R.id.time
  }
  
  init {
    id = R.id.hots
    layoutParams = LayoutParams(matchParent, wrapContent)
    
    addView(image, constraintLayoutParams(matchConstraint, 200.dp) {
      startToStart = parentId
      topToTop = parentId
      endToEnd = parentId
    })
    
    addView(title, constraintLayoutParams(matchConstraint, wrapContent) {
      startToStart = parentId
      topToBottomOf(image)
      endToEnd = parentId
      marginStart = 8.dp
      marginEnd = 8.dp
      topMargin = 10.dp
    })
    
    addView(time, constraintLayoutParams(matchConstraint, wrapContent) {
      startToStartOf(title)
      topToBottomOf(title)
      endToEndOf(title)
      bottomToBottom = parentId
      topMargin = 10.dp
      bottomMargin = 10.dp
    })
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
        addView(HotsView(context), frameLayoutParams(matchParent, wrapContent))
      }
    }
  }
}