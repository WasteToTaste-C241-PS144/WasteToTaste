package com.capstone.wastetotaste.ui.recipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.wastetotaste.MainActivity
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.ViewModelFactory
import com.capstone.wastetotaste.adapter.RecipeAdapter
import com.capstone.wastetotaste.api.IngredientsRequest
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.FragmentRecipeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class RecipeFragment : Fragment() {

    private lateinit var binding: FragmentRecipeBinding
    private lateinit var viewModel: RecipeViewModel
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecipeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipe.layoutManager = layoutManager

        viewModel = obtainViewModel(this)

        viewModel.isSearching.value = false

        viewModel.allIngredients.observe(viewLifecycleOwner, Observer { ingredients ->
            if (ingredients.isNotEmpty()) {
                viewModel.predictRecipe(IngredientsRequest(ingredients))
                binding.noRecommendationFound.visibility = View.GONE
                binding.nsvRecipe.visibility = View.VISIBLE
            }
            else{
                viewModel.emptyRecipe()
                binding.noRecommendationFound.visibility = View.VISIBLE
                binding.nsvRecipe.visibility = View.GONE
            }
        })

        binding.recipeSearchBar.imeOptions = EditorInfo.IME_ACTION_SEARCH

        binding.recipeSearchBar.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                return when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        val inputText = binding.recipeSearchBar.text.toString().trim()
                        if(inputText.isNotEmpty()){
                            viewModel.searchRecipe(inputText)
                        } else  viewModel.searchRecipe("*")
                        binding.recipeSearchBar.clearFocus()
                        hideKeyboard(binding.recipeSearchBar, requireActivity())
                        true
                    }
                    else -> false
                }
            }
        })

        adapter = RecipeAdapter(viewModel)
        binding.rvRecipe.adapter = adapter

        viewModel.recipePrediction.observe(viewLifecycleOwner) { listRecipe ->
            viewModel.isSearching.observe(viewLifecycleOwner) {isSearching ->
                if(listRecipe.isEmpty() && isSearching){
                    binding.noRecipeFound.visibility = View.VISIBLE
                    binding.noRecommendationFound.visibility = View.GONE
                    binding.nsvRecipe.visibility = View.GONE
                }
                else if(listRecipe.isNotEmpty() && isSearching){
                    binding.noRecipeFound.visibility = View.GONE
                    binding.noRecommendationFound.visibility = View.GONE
                    binding.nsvRecipe.visibility = View.VISIBLE
                    binding.tvBasedOnYourIngredients.visibility = View.GONE
                }
            }
            adapter.setRecipe(listRecipe)
        }

        adapter.setOnItemClickCallback(object : RecipeAdapter.OnItemClickCallback {
            override fun onItemClicked(recipe: Recipe) {
                val moveWithObjectIntent = Intent(requireContext(), RecipeDetailActivity::class.java)
                moveWithObjectIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe)
                startActivity(moveWithObjectIntent)
            }
        })
        binding.btnDeleteSearch.setOnClickListener {
            binding.recipeSearchBar.text.clear()
            binding.noRecipeFound.visibility = View.GONE
            viewModel.isSearching.value = false
            viewModel.allIngredients.observe(viewLifecycleOwner, Observer { ingredients ->
                if (ingredients.isNotEmpty()) {
                    viewModel.predictRecipe(IngredientsRequest(ingredients))
                    binding.noRecommendationFound.visibility = View.GONE
                    binding.tvBasedOnYourIngredients.visibility = View.VISIBLE
                    binding.nsvRecipe.visibility = View.VISIBLE
                }
                else{
                    viewModel.emptyRecipe()
                    binding.noRecommendationFound.visibility = View.VISIBLE
                    binding.nsvRecipe.visibility = View.GONE
                }
            })
        }
        binding.btnGoToPantry.setOnClickListener{
            (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)?.selectedItemId = R.id.navigation_pantry
        }

        binding.recipeSearchBar.setSaveEnabled(false)
    }

    private fun obtainViewModel(fragment: Fragment): RecipeViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment.requireActivity(), factory)[RecipeViewModel::class.java]
    }

    private fun hideKeyboard(view: View, activity: FragmentActivity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


