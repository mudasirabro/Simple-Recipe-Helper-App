package com.example.simplerecipehelper.ui.recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplerecipehelper.data.Recipe
import com.example.simplerecipehelper.databinding.ItemRecipeBinding

class RecipesListAdapter(
    private val onRecipeClick: (Recipe) -> Unit
) : ListAdapter<Recipe, RecipesListAdapter.RecipeViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean =
            oldItem == newItem
    }

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recipe) {
            binding.recipeNameTextView.text = item.name
            binding.ingredientsPreviewTextView.text = item.ingredients
            binding.root.setOnClickListener { onRecipeClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


