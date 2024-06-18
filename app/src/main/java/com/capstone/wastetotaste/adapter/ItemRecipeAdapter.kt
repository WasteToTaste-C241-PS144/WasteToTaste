package com.capstone.wastetotaste.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.ItemRecipeBinding

class ItemRecipeAdapter(private var recipes: List<Recipe>, private val onItemClickCallback: OnItemClickCallback) :
    RecyclerView.Adapter<ItemRecipeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(recipe)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setRecipes(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.tvTitle.text = recipe.title
            // Bind other views as needed
            Glide.with(itemView)
                .load(recipe.imgUrl)
                .into(binding.imgItemPhoto)

        }
        }
    interface OnItemClickCallback {
        fun onItemClicked(recipe: Recipe)
    }
    }