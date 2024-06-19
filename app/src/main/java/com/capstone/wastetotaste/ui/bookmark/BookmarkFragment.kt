package com.capstone.wastetotaste.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.wastetotaste.adapter.ItemRecipeAdapter
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.databinding.FragmentBookmarkBinding
import com.capstone.wastetotaste.ui.recipe.RecipeDetailActivity


class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding
    private val viewModel: BookmarkViewModel by viewModels {
        BookmarkViewModelFactory()
    }
    private lateinit var adapter: ItemRecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ItemRecipeAdapter(emptyList(), object : ItemRecipeAdapter.OnItemClickCallback {
            override fun onItemClicked(recipe: Recipe) {
                val moveWithObjectIntent = Intent(requireContext(), RecipeDetailActivity::class.java)
                moveWithObjectIntent.putExtra(RecipeDetailActivity.EXTRA_RECIPE, recipe)
                startActivity(moveWithObjectIntent)
            }
        })

        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.layoutManager = LinearLayoutManager(requireContext())

        viewModel.bookmarkRecipes.observe(viewLifecycleOwner, Observer { recipes ->
            adapter.setRecipes(recipes) // Use setter method to update adapter data
        })

    }
}
