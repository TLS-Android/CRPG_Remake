package com.tiagosantos.crpg_remake.ui.reminders

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.plataforma.crpg.databinding.ItemRowBinding

class ChildAdapter(private val delegate: Delegate) : RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {

  private val items: ArrayList<String> = arrayListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
    val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ChildViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
    with(holder.binding) {
      rowTitle.text = items[position]
      root.setOnClickListener {
        delegate.onRowItemClick(position, items[position], root.context)
      }
    }
  }

  fun addItemList(itemList: List<String>) {
    items.clear()
    items.addAll(itemList)
    notifyDataSetChanged()
  }

  override fun getItemCount() = items.size

  class ChildViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

  interface Delegate {
    fun onRowItemClick(position: Int, title: String, context: Context)
  }
}
