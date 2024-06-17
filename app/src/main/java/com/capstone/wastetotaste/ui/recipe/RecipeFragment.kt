package com.capstone.wastetotaste.ui.recipe

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.ViewModelFactory
import com.capstone.wastetotaste.adapter.RecipeAdapter
import com.capstone.wastetotaste.api.IngredientsRequest
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.FragmentHomeBinding
import com.capstone.wastetotaste.databinding.FragmentRecipeBinding
import com.capstone.wastetotaste.ui.home.HomeViewModel

class RecipeFragment : Fragment() {

    private lateinit var binding: FragmentRecipeBinding

    companion object {
        fun newInstance() = RecipeFragment()
    }

    private lateinit var viewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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

        viewModel.allIngredients.observe(requireActivity(), Observer { ingredients ->
            if(ingredients.isNotEmpty()) viewModel.predictRecipe(IngredientsRequest(ingredients))
        })

        val adapter = RecipeAdapter(viewModel)
        binding.rvRecipe.adapter = adapter

        viewModel.recipePrediction.observe(requireActivity()){ listRecipe ->
            adapter.setRecipe(listRecipe)
        }

        adapter.setOnItemClickCallback(object : RecipeAdapter.OnItemClickCallback {
            override fun onItemClicked(recipe: Recipe) {
                val moveWithObjectIntent = Intent(requireContext(), RecipeDetailActivity::class.java)
                moveWithObjectIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe)
                startActivity(moveWithObjectIntent)
            }
        })

    }
    private fun obtainViewModel(fragment: Fragment): RecipeViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment.requireActivity(), factory)[RecipeViewModel::class.java]
    }
}