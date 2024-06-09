package com.capstone.wastetotaste.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.database.Ingredients
import com.capstone.wastetotaste.databinding.ItemPantry2Binding
import com.capstone.wastetotaste.ui.pantry.PantryViewModel
import java.text.SimpleDateFormat
import java.util.*

class IngredientAdapter(private val viewModel: PantryViewModel) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    private var ingredients = emptyList<Ingredients>()

    inner class IngredientViewHolder(private val binding: ItemPantry2Binding) : RecyclerView.ViewHolder(binding.root){
        fun bind(ingredient: Ingredients) {
            binding.tvIngredients.text = ingredient.name
            binding.btnDelete.setOnClickListener{
                viewModel.delete(ingredient)
            }
//            binding.btnEdit.setOnClickListener{
//
//            }
            //binding.tvExpiredDate.text = ingredient.expiryDate.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = ItemPantry2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
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