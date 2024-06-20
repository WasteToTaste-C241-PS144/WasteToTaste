package com.capstone.wastetotaste.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.ItemRecipeBinding
import com.capstone.wastetotaste.ui.home.RecipeResultViewModel
import com.capstone.wastetotaste.ui.recipe.RecipeViewModel

class RecipeResultAdapter(private val recipeResultViewModel: RecipeResultViewModel) : RecyclerView.Adapter<RecipeResultAdapter.RecipeViewHolder>() {

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

    inner class RecipeViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.tvTitle.text = recipe.title
            Glide.with(itemView)
                .load(recipe.imgUrl)
                .into(binding.imgItemPhoto)

            // Clear previous listener
            binding.tbBookmark.setOnCheckedChangeListener(null)

            // Set initial state of toggle button
            binding.tbBookmark.isChecked = recipe.isBookmarked
            setBookmarkIcon(binding.tbBookmark, recipe.isBookmarked)

            // Add new listener
            binding.tbBookmark.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked != recipe.isBookmarked) {
                    recipe.isBookmarked = isChecked
                    setBookmarkIcon(binding.tbBookmark, isChecked)
                    if (isChecked) {
                        recipeResultViewModel.addToBookmarks(recipe)
                    } else {
                        recipeResultViewModel.removeFromBookmarks(recipe)
                    }
                }
            }

            binding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(recipe)
            }
        }

        private fun setBookmarkIcon(toggleButton: ToggleButton, isBookmarked: Boolean) {
            if (isBookmarked) {
                toggleButton.setBackgroundResource(R.drawable.ic_bookmark_filled)
            } else {
                toggleButton.setBackgroundResource(R.drawable.ic_bookmark)
            }
        }
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setRecipe(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }
}

