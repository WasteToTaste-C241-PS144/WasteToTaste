package com.capstone.wastetotaste.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.ItemRecipeBinding
import com.capstone.wastetotaste.ui.recipe.RecipeViewModel

class RecipeAdapter(recipeViewModel: RecipeViewModel) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipes = emptyList<Recipe>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(recipe: Recipe)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeAdapter.RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }
    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(recipe: Recipe) {
            binding.tvTitle.text = recipe.title
            binding.apply {
                Glide.with(itemView)
                    .load(recipe.imgUrl)
                    .into(imgItemPhoto)

                tvTitle.text = recipe.title
            }
        }
    }

    override fun onBindViewHolder(holder: RecipeAdapter.RecipeViewHolder, position: Int) {
        val currentItem = recipes[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(recipes[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setRecipe(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

}