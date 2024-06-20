package com.capstone.wastetotaste.ui.recipe

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.ActivityRecipeDetailBinding
import com.bumptech.glide.Glide
import com.capstone.wastetotaste.R

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding
    private lateinit var recipe: Recipe
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipe = intent.getParcelableExtra(EXTRA_RECIPE)!!

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        populateRecipeDetails(recipe)

        updateBookmarkState()

        binding.toggleBookmark.setOnClickListener {
            toggleBookmarkState()
        }
    }

    private fun populateRecipeDetails(recipe: Recipe) {
        Glide.with(this)
            .load(recipe.imgUrl)
            .into(binding.ivRecipe)
        binding.tvRecipeName.text = recipe.title
        binding.tvIngredients.text = recipe.ingredients?.joinToString("\n") { it ?: "" }
        val steps = recipe.steps?.filter { !it.isNullOrBlank() }
            ?.mapIndexed { index, step -> "${index + 1}. $step" }
            ?.joinToString("\n")

        binding.tvInstruction.text = steps
    }

    private fun updateBookmarkState() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val recipeDocRef = firestore.collection("users").document(userId).collection("bookmarks").document(recipe.id.toString())

            // Get latest data from Firestore
            recipeDocRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val updatedRecipe = documentSnapshot.toObject(Recipe::class.java)
                        updatedRecipe?.let {
                            recipe.isBookmarked = it.isBookmarked // Update local recipe with Firestore data
                            //val iconResId = if (recipe.isBookmarked) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark
                            //binding.toggleBookmark.setBackgroundResource(iconResId)

                            binding.toggleBookmark.setBackgroundResource(
                                if (recipe.isBookmarked)
                                    R.drawable.ic_bookmark_filled // Change to the eye closed icon
                                else
                                    R.drawable.ic_bookmark // Change to the eye open icon
                            )
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error fetching recipe details", exception)
                }
        }

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun toggleBookmarkState() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val originalBookmarkState = recipe.isBookmarked // Simpan status bookmark sebelum diubah
            recipe.isBookmarked = !recipe.isBookmarked // Toggle bookmark status lokal

            val recipeDocRef = firestore.collection("users").document(userId).collection("bookmarks").document(recipe.id.toString())

            if (!recipe.isBookmarked) {
                // Hapus dokumen dari Firestore
                recipeDocRef.delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "Recipe deleted successfully from Firestore")

                        // Update UI state setelah penghapusan berhasil
                        updateBookmarkState()

                        // Periksa apakah dokumen masih ada di Firestore
                        recipeDocRef.get().addOnSuccessListener { documentSnapshot ->
                            if (!documentSnapshot.exists()) {
                                binding.toggleBookmark.setBackgroundResource(R.drawable.ic_bookmark)
                            }
                        }.addOnFailureListener { exception ->
                            Log.e(TAG, "Error checking document existence in Firestore", exception)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error deleting recipe from Firestore", e)
                        recipe.isBookmarked = originalBookmarkState // Rollback
                    }
            } else {
                recipeDocRef.set(recipe)
                    .addOnSuccessListener {
                        updateBookmarkState()
                        Log.d(TAG, "Recipe bookmark status updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error updating recipe bookmark status", e)
                        recipe.isBookmarked = originalBookmarkState // Rollback
                    }
            }
        }
    }

    companion object {
        const val EXTRA_RECIPE = "extra_recipe"
        const val TAG = "RecipeDetailActivity"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
