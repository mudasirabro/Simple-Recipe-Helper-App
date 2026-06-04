package com.example.simplerecipehelper.ui.shopping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplerecipehelper.data.ShoppingItem
import com.example.simplerecipehelper.databinding.ItemShoppingBinding

class ShoppingListAdapter(
    private val onCheckedChange: (ShoppingItem) -> Unit,
    private val onDeleteClick: (ShoppingItem) -> Unit
) : ListAdapter<ShoppingItem, ShoppingListAdapter.ViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean =
            oldItem == newItem
    }

    inner class ViewHolder(private val binding: ItemShoppingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingItem) {
            binding.checkBox.text = item.name
            binding.checkBox.isChecked = item.isChecked

            binding.checkBox.setOnCheckedChangeListener { _, _ ->
                onCheckedChange(item)
            }

            binding.deleteButton.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemShoppingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


