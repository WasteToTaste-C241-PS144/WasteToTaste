package com.capstone.wastetotaste.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.database.Ingredients
import com.capstone.wastetotaste.databinding.ItemIngredientsBinding
import com.capstone.wastetotaste.databinding.ItemPantry2Binding
import com.capstone.wastetotaste.ui.home.HomeViewModel
import com.capstone.wastetotaste.ui.pantry.PantryViewModel
import java.text.SimpleDateFormat
import java.util.*

class IngredientsHomeAdapter(private val viewModel: HomeViewModel) : RecyclerView.Adapter<IngredientsHomeAdapter.IngredientViewHolder>() {

    private var ingredients = emptyList<Ingredients>()

    inner class IngredientViewHolder(private val binding: ItemIngredientsBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(ingredient: Ingredients) {
            binding.tvIngredientsHome.text = ingredient.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = ItemIngredientsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentItem = ingredients[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    fun setIngredients(ingredients: List<Ingredients>) {
        this.ingredients = ingredients
        notifyDataSetChanged()
    }
}

