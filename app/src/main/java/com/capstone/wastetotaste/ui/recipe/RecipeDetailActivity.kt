package com.capstone.wastetotaste.ui.recipe

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.ActivityAccountInfoBinding
import com.capstone.wastetotaste.databinding.ActivityRecipeDetailBinding

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding
    companion object {
        const val EXTRA_RECIPE = "extra_recipe"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recipe = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra<Recipe>(EXTRA_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<Recipe>(EXTRA_RECIPE)
        }
        if (recipe != null) {
            val formattedIngredients = recipe.ingredients?.joinToString("\n") {
                it ?: ""
            }
            val formattedSteps = recipe.steps
                ?.filter { it?.isNotEmpty() ?: false }
                ?.mapIndexedNotNull { index, step ->
                    val formattedStep = step?.let { "${index + 1}. $it\n" }
                    formattedStep
                }
                ?.joinToString("")

            binding.apply {
                Glide.with(this@RecipeDetailActivity)
                    .load(recipe.imgUrl)
                    .into(ivRecipe)
                tvRecipeName.text = recipe.title
                tvIngredients.text = formattedIngredients
                tvInstruction.text = formattedSteps
            }
        }
    }


}