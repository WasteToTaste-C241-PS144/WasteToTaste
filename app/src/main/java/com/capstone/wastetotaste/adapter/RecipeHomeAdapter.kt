package com.capstone.wastetotaste.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.ItemRecipePreviewBinding
import com.capstone.wastetotaste.ui.home.HomeViewModel

class RecipeHomeAdapter(private val homeViewModel: HomeViewModel) : RecyclerView.Adapter<RecipeHomeAdapter.RecipeViewHolder>() {

    private var recipes = emptyList<Recipe>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(recipe: Recipe)
    }
    inner class RecipeViewHolder(private val binding: ItemRecipePreviewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(recipe: Recipe) {
            binding.apply {
                Glide.with(itemView)
                    .load(recipe.imgUrl)
                    .into(imgRecipePhotoHome)

                tvRecipeTitleHome.text = recipe.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHomeAdapter.RecipeViewHolder {
        val binding = ItemRecipePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecipeHomeAdapter.RecipeViewHolder, position: Int) {
        if(holder is RecipeViewHolder){
            val currentItem = recipes[position]
            holder.bind(currentItem)
            holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(recipes[holder.adapterPosition]) }
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun getItemViewType(position: Int): Int {
        //return if (position == 0) RecipeHomeAdapter.VIEW_TYPE_EMPTY else RecipeHomeAdapter.VIEW_TYPE_ITEM
        return RecipeHomeAdapter.VIEW_TYPE_ITEM
    }
    fun setRecipe(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_EMPTY = 1
    }
}