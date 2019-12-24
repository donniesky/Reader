package me.donnie.reader.hots

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import me.donnie.reader.R
import me.donnie.reader.data.entities.Article
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
  
  val scrim = View(context).apply {
    id = R.id.scrim
  }
  
  val title = TextView(context).apply {
    id = R.id.title
    textAppearance = R.style.TextAppearance_MaterialComponents_Headline6
  }
  
  val time = TextView(context).apply {
    id = R.id.time
    textAppearance = R.style.TextAppearance_MaterialComponents_Caption
  }
  
  init {
    id = R.id.hots
    layoutParams = LayoutParams(matchParent, wrapContent)
    ViewCompat.setBackground(scrim, DrawableUtils.makeScrimDrawable())
    
    addView(image, constraintLayoutParams(matchConstraint, 200.dp) {
      startToStart = parentId
      topToTop = parentId
      endToEnd = parentId
    })
    
    addView(scrim, constraintLayoutParams(matchConstraint, matchConstraint) {
      topToTopOf(image)
      bottomToBottomOf(image)
      startToStart = parentId
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
          topMargin = 8.dp
          bottomMargin = 8.dp
        }
        radius = 3.dp.toFloat()
        elevation = 3.dp.toFloat()
        addView(HotsView(context), frameLayoutParams(matchParent, wrapContent))
      }
    }
  }
}