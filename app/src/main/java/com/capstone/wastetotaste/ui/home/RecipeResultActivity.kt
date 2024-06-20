package com.capstone.wastetotaste.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.wastetotaste.MainActivity
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.ViewModelFactory
import com.capstone.wastetotaste.adapter.RecipeAdapter
import com.capstone.wastetotaste.adapter.RecipeResultAdapter
import com.capstone.wastetotaste.api.IngredientsRequest
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.ActivityRecipeBinding
import com.capstone.wastetotaste.ui.recipe.RecipeDetailActivity
import com.capstone.wastetotaste.ui.recipe.RecipeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeBinding
    private var hint: String? = ""
    private lateinit var viewModel: RecipeResultViewModel
    private lateinit var adapter: RecipeResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppBar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        viewModel = obtainViewModel(this)
        hint = intent.getStringExtra(EXTRA_HINT).toString()
        binding.recipeHomeSearchBarResult.setText(hint)
        val inputText = binding.recipeHomeSearchBarResult.text.toString()
        if(inputText.isNotBlank()) {
            viewModel.searchRecipe(inputText)
        } else {
            binding.homeNoRecipeFound.visibility = View.VISIBLE
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvHomeRecipeSearchResult.layoutManager = layoutManager

        adapter = RecipeResultAdapter(viewModel)
        binding.rvHomeRecipeSearchResult.adapter = adapter

        binding.recipeHomeSearchBarResult.imeOptions = EditorInfo.IME_ACTION_SEARCH

        binding.recipeHomeSearchBarResult.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                return when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        val inputText = binding.recipeHomeSearchBarResult.text.toString().trim()
                        if(inputText.isNotBlank()){
                            viewModel.searchRecipe(inputText)
                        } else {
                            viewModel.searchRecipe("*")
                        }
                        binding.recipeHomeSearchBarResult.clearFocus()
                        hideKeyboard(binding.recipeHomeSearchBarResult, this@RecipeResultActivity)
                        true
                    }
                    else -> false
                }
            }
        })

        viewModel.searchResult.observe(this) { listRecipe ->
            if(listRecipe.isEmpty()){
                binding.homeNoRecipeFound.visibility = View.VISIBLE
            }
            else{
                binding.homeNoRecipeFound.visibility = View.GONE
            }
            adapter.setRecipe(listRecipe)
        }

        adapter.setOnItemClickCallback(object : RecipeResultAdapter.OnItemClickCallback {
            override fun onItemClicked(recipe: Recipe) {
                val moveWithObjectIntent = Intent(this@RecipeResultActivity, RecipeDetailActivity::class.java)
                moveWithObjectIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe)
                startActivity(moveWithObjectIntent)
            }
        })

        binding.recipeHomeSearchBarResult.setSaveEnabled(false)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            removeBottomConstraint()
            setLeftRightMargins(binding.ivNoRecipeFound, 500, 500, 70)
        }
    }

    private fun obtainViewModel(activity: Activity): RecipeResultViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(this, factory)[RecipeResultViewModel::class.java]
    }

    private fun hideKeyboard(view: View, activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    companion object {
        const val EXTRA_HINT = "extra_hint"
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            removeBottomConstraint()
            setLeftRightMargins(binding.ivNoRecipeFound, 500, 500, 70)
        }
    }

    private fun removeBottomConstraint() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.homeSearchResultLayout)
        constraintSet.clear(binding.homeNoRecipeFound.id, ConstraintSet.BOTTOM)
        constraintSet.applyTo(binding.homeSearchResultLayout)
    }

    private fun setLeftRightMargins(view: View, left: Int, right: Int, top: Int) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        params.leftMargin = left
        params.rightMargin = right
        params.topMargin = top
        view.layoutParams = params
    }
}