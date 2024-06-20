package com.capstone.wastetotaste.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.wastetotaste.database.Ingredients
import com.capstone.wastetotaste.databinding.ItemEmptyBinding
import com.capstone.wastetotaste.databinding.ItemIngredientsPreviewBinding
import com.capstone.wastetotaste.ui.pantry.PantryViewModel

class IngredientsAdapter(private val viewModel: PantryViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var ingredients = emptyList<Ingredients>()

    inner class IngredientViewHolder(private val binding: ItemIngredientsPreviewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(ingredient: Ingredients) {
            binding.tvIngredients.text = ingredient.name
            binding.btnDelete.setOnClickListener{
                viewModel.delete(ingredient)
            }
        }
    }
    inner class EmptyViewHolder(private val binding: ItemEmptyBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = ItemIngredientsPreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            IngredientViewHolder(binding)
        } else {
            val binding = ItemEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            EmptyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is IngredientViewHolder){
            val currentItem = ingredients[position]
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return if (ingredients.size > MIN_ITEMS) ingredients.size+1 else ingredients.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < ingredients.size) VIEW_TYPE_ITEM else VIEW_TYPE_EMPTY
    }

    fun setIngredients(ingredients: List<Ingredients>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }

    companion object {
        private const val MIN_ITEMS = 8
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_EMPTY = 1
    }
}