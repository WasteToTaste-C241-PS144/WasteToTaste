package com.capstone.wastetotaste.ui.adapter

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.wastetotaste.data.response.ResultsItem
import com.capstone.wastetotaste.databinding.ItemRecipeBinding

class RecipeAdapter : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    private var list = ArrayList<ResultsItem>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback (onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: ArrayList<ResultsItem>){
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class  RecipeViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(recipe: ResultsItem){
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(recipe)
            }

            binding.apply {
                Glide.with(itemView)
                    .load(recipe.image)
                    .into(imgItemPhoto)

                tvTitle.text = recipe.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(list[position])
    }

    interface OnItemClickCallback{
        fun onItemClicked(data: ResultsItem)
    }

    override fun getItemCount(): Int = list.size
}