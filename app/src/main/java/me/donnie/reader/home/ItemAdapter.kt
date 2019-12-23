package me.donnie.reader.home

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.donnie.reader.R
import me.donnie.reader.data.entities.Item

class ItemAdapter : ListAdapter<Item, ItemViewHolder>(ItemsCallback) {
  
  init {
    setHasStableIds(true)
  }
  
  override fun getItemId(position: Int): Long {
    return position.toLong()
  }
  
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    return ItemViewHolder(HomeView.newCardItem(parent.context))
  }
  
  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.home.bindItem(getItem(position))
  }
  
  override fun onViewRecycled(holder: ItemViewHolder) {
    holder.home.unbind()
  }
}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val home: HomeView = itemView.findViewById(R.id.home)
}

private object ItemsCallback : DiffUtil.ItemCallback<Item>() {
  override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
    return oldItem.guid == newItem.guid
  }
  
  override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
    return oldItem == newItem
  }
  
  override fun getChangePayload(oldItem: Item, newItem: Item): Any? {
    if (oldItem == newItem) {
      return null
    }
    return Unit
  }
}