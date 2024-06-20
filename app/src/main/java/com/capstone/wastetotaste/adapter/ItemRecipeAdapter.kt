package com.capstone.wastetotaste.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.ItemRecipeBinding
import com.capstone.wastetotaste.ui.recipe.RecipeDetailActivity.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ItemRecipeAdapter(
    private var recipes: List<Recipe>,
    private val onItemClickCallback: OnItemClickCallback
) : RecyclerView.Adapter<ItemRecipeAdapter.ViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(recipe: Recipe)
    }

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

        holder.binding.tbBookmark.setOnClickListener {
            toggleBookmarkState(holder.itemView.context, recipe)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setRecipes(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    private fun toggleBookmarkState(context: Context, recipe: Recipe) {
        recipe.isBookmarked = !recipe.isBookmarked // Toggle bookmark state locally
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { currentUser ->
            val userId = currentUser.uid
            val bookmarksCollection = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("bookmarks")
                .document(recipe.id.toString())

            if (recipe.isBookmarked) {
                bookmarksCollection.set(recipe)
                    .addOnSuccessListener {
                        showToast(context, "Resep berhasil dibookmark")
                    }
                    .addOnFailureListener { e ->
                        showToast(context, "Resep gagal dibookmark: ${e.message}")
                        recipe.isBookmarked = !recipe.isBookmarked // Rollback on failure
                    }
            } else {
                bookmarksCollection.delete()
                    .addOnSuccessListener {
                        showToast(context, "Resep berhasil dihapus dari bookmark")
                    }
                    .addOnFailureListener { e ->
                        showToast(context, "Resep gagal dihapus dari bookmark: ${e.message}")
                        recipe.isBookmarked = !recipe.isBookmarked // Rollback on failure
                    }
            }
        }
    }
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.tvTitle.text = recipe.title
            Glide.with(binding.root)
                .load(recipe.imgUrl)
                .into(binding.imgItemPhoto)

            val iconResId = if (recipe.isBookmarked) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark
            binding.tbBookmark.setBackgroundResource(iconResId)
        }
    }
}
