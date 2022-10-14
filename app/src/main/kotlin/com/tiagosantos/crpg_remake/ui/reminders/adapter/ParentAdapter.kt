package com.plataforma.crpg.ui.reminders.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.plataforma.crpg.R
import com.plataforma.crpg.databinding.ItemSectionBinding
import com.plataforma.crpg.model.SectionItem

class ParentAdapter :
    RecyclerView.Adapter<ParentAdapter.ParentViewHolder>(), ChildAdapter.Delegate {

    private val items: ArrayList<SectionItem> = arrayListOf()
    private val childAdapter = ChildAdapter(this)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ItemSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(binding).apply {
            with(binding.root) {
                setOnClickListener { toggleLayout() }
            }
        }
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val sectionItem = items[position]
        with(holder.binding.expandableLayout) {
            parentLayoutResource = R.layout.item_section_parent
            secondLayoutResource = R.layout.item_section_child
            duration = 200L
            parentLayout.findViewById<TextView>(R.id.title).text = sectionItem.title
            parentLayout.setBackgroundColor(ContextCompat.getColor(context, sectionItem.color))
            secondLayout.findViewById<RecyclerView>(R.id.recyclerViewChild).adapter = childAdapter
            childAdapter.addItemList(sectionItem.itemList)
        }
    }

    override fun onRowItemClick(position: Int, title: String, context: Context) {
        Toast.makeText(context, "position : $position, title: $title", Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount() = items.size

    class ParentViewHolder(val binding: ItemSectionBinding) :
        RecyclerView.ViewHolder(binding.root)
}
