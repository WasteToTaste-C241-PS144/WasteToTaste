package com.capstone.wastetotaste.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.ItemRecipeBinding
import com.capstone.wastetotaste.ui.recipe.RecipeViewModel

class RecipeAdapter(private val recipeViewModel: RecipeViewModel) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private var recipes = emptyList<Recipe>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(recipe: Recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    inner class RecipeViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.tvTitle.text = recipe.title
            Glide.with(itemView)
                .load(recipe.imgUrl)
                .into(binding.imgItemPhoto)

            // Set initial bookmark state
            if (recipe.isBookmarked) {
                binding.ivBookmark.setImageResource(R.drawable.ic_bookmark_filled)
            } else {
                binding.ivBookmark.setImageResource(R.drawable.ic_bookmark)
            }


            binding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(recipe)
            }
        }
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setRecipe(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }
}
