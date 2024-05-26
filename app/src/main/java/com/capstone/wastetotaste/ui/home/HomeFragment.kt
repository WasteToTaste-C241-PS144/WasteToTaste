package com.capstone.wastetotaste.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.wastetotaste.R
import com.capstone.wastetotaste.data.response.RecipeResponse
import com.capstone.wastetotaste.data.retrofit.ApiConfig
import com.capstone.wastetotaste.databinding.FragmentHomeBinding
import com.capstone.wastetotaste.ui.adapter.RecipeAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecipeAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvSearchRecipe.layoutManager = LinearLayoutManager(context)
            rvSearchRecipe.setHasFixedSize(true)
            rvSearchRecipe.adapter = adapter
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    val title = searchView.text.toString()
                    findRecipe(title)
                    searchView.hide()
                    false
                }
        }
    }

    private fun findRecipe (title: String){
        val client = ApiConfig.apiInstance.searchRecipe(title)
        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(
                call: Call<RecipeResponse>,
                response: Response<RecipeResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        //val totalCount = responseBody.totalResults
                        adapter.setList(responseBody.results)
                    }
                } else {
                    Log.e("TAG", "onFailure: ${response.message()}")
                    // Show error message to user
                    showToast("Failed to search users. Please try again later.")
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.e("TAG", "onFailure: ${t.message}")
                // Show error message to user
                showToast("Failed to search recipes. Please check your internet connection.")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}