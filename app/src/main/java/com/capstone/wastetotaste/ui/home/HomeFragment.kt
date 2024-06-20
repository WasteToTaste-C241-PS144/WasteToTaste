package com.capstone.wastetotaste.ui.home

import android.R.id.hint
import android.R.id.message
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.wastetotaste.MainActivity
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.UserPreferencesManager
import com.capstone.wastetotaste.ViewModelFactory
import com.capstone.wastetotaste.adapter.IngredientsHomeAdapter
import com.capstone.wastetotaste.adapter.RecipeHomeAdapter
import com.capstone.wastetotaste.api.IngredientsRequest
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.FragmentHomeBinding
import com.capstone.wastetotaste.ui.authentication.AuthSplashVM
import com.capstone.wastetotaste.ui.authentication.UserVMFactory
import com.capstone.wastetotaste.ui.authentication.dataStore
import com.capstone.wastetotaste.ui.recipe.RecipeDetailActivity
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var authSplashVM: AuthSplashVM
    private lateinit var itemAdapter: IngredientsHomeAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inisialisasi UserPreferencesManager
        val userPreferencesManager = UserPreferencesManager.getInstance(requireContext().dataStore)

        // Inisialisasi ViewModel
        authSplashVM = ViewModelProvider(this, UserVMFactory(userPreferencesManager)).get(
            AuthSplashVM::class.java)

        // Mengamati data nama pengguna dari ViewModel
        authSplashVM.getName().observe(viewLifecycleOwner, { name ->
            name?.let {
                binding.greetings.text = getString(R.string.halouser, it)            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = obtainViewModel(this)
        itemAdapter = IngredientsHomeAdapter(homeViewModel)
        homeViewModel.allIngredients.observe(requireActivity(), Observer { ingredients ->
            if (ingredients.isNotEmpty()) {
                ingredients?.let { itemAdapter.setIngredients(it) }
                homeViewModel.predictRecipe(IngredientsRequest(ingredients))
                binding.homeEmpty.visibility = View.GONE
                binding.homePreview.visibility = View.VISIBLE
            }
            else{
                homeViewModel.emptyRecipe()
                binding.homeEmpty.visibility = View.VISIBLE
                binding.homePreview.visibility = View.GONE
            }
        })
        binding.tvPantrySeeAll.setOnClickListener{
            (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)?.selectedItemId = R.id.navigation_pantry
        }
        binding.tvRecipeSeeAll.setOnClickListener{
            (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)?.selectedItemId = R.id.navigation_recipe
        }

        val ingredientsLayoutManager = FlexboxLayoutManager(requireContext())
        ingredientsLayoutManager.flexDirection = FlexDirection.ROW
        binding.rvHomeIngredients.setLayoutManager(ingredientsLayoutManager)
        binding.rvHomeIngredients.adapter = itemAdapter

        val recipeLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHomeRecipe.layoutManager = recipeLayoutManager

        val adapter = RecipeHomeAdapter(homeViewModel)
        binding.rvHomeRecipe.adapter = adapter

        homeViewModel.recipePrediction.observe(requireActivity()){ listRecipe ->
            adapter.setRecipe(listRecipe)
        }

        adapter.setOnItemClickCallback(object : RecipeHomeAdapter.OnItemClickCallback {
            override fun onItemClicked(recipe: Recipe) {
                val moveWithObjectIntent = Intent(requireContext(), RecipeDetailActivity::class.java)
                moveWithObjectIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe)
                startActivity(moveWithObjectIntent)
            }
        })

        binding.btnGoToPantryHome.setOnClickListener {
            (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.nav_view)?.selectedItemId = R.id.navigation_pantry
        }

        binding.recipeHomeSearchBar.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                return when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        val inputText = binding.recipeHomeSearchBar.text.toString().trim()
                        val moveWithObjectIntent = Intent(requireContext(), RecipeResultActivity::class.java)
                        moveWithObjectIntent.putExtra(RecipeResultActivity.EXTRA_HINT, inputText)
                        startActivity(moveWithObjectIntent)
                        true
                    }
                    else -> false
                }
            }
        })

        binding.recipeHomeSearchBar.setSaveEnabled(false)
    }

    private fun obtainViewModel(fragment: Fragment): HomeViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment.requireActivity(), factory)[HomeViewModel::class.java]
    }

}