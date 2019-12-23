package me.donnie.reader.hots

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.donnie.reader.R
import me.donnie.reader.data.entities.Article

class HotsAdapter : ListAdapter<Article, ItemViewHolder>(ItemsCallback) {
  
  init {
    setHasStableIds(true)
  }
  
  override fun getItemId(position: Int): Long {
    return position.toLong()
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    return ItemViewHolder(HotsView.newCardItem(parent.context))
  }
  
  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.hots.bindArticle(getItem(position))
  }
  
  override fun onViewRecycled(holder: ItemViewHolder) {
    holder.hots.unbind()
  }
}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val hots: HotsView = itemView.findViewById(R.id.hots)
}

private object ItemsCallback : DiffUtil.ItemCallback<Article>() {
  override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
    return oldItem.id == newItem.id
  }
  
  override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
    return oldItem == newItem
  }
  
  override fun getChangePayload(oldItem: Article, newItem: Article): Any? {
    if (oldItem == newItem) {
      return null
    }
    return Unit
  }
}