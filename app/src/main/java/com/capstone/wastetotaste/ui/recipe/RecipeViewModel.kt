package com.capstone.wastetotaste.ui.recipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.wastetotaste.api.ApiConfig
import com.capstone.wastetotaste.api.IngredientsRequest
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.data.RecipeResponse
import com.capstone.wastetotaste.repository.IngredientsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Query

class RecipeViewModel(application: Application) : ViewModel() {
    val _recipePrediction = MutableLiveData<List<Recipe>>()
    private val mIngredientsRepository: IngredientsRepository = IngredientsRepository(application)
    val allIngredients: LiveData<List<String>> = mIngredientsRepository.getAllIngredientsName()
    val isSearching = MutableLiveData<Boolean>()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun predictRecipe(request: IngredientsRequest) {
        val client = ApiConfig.getApiPredictService().predictRecipe(request)
        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _recipePrediction.value = responseBody.data
                        checkBookmarks()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun addToBookmarks(recipe: Recipe) {
        val userId = auth.currentUser?.uid
        userId?.let {
            firestore.collection("users").document(it)
                .collection("bookmarks").document(recipe.id.toString())
                .set(recipe)
                .addOnSuccessListener {
                    Log.d(TAG, "Recipe added to bookmarks")
                    updateRecipeInPredictionList(recipe.id!!, true)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error adding recipe to bookmarks", e)
                }
        } ?: run {
            Log.e(TAG, "Current user is null")
        }
    }

    fun removeFromBookmarks(recipe: Recipe) {
        val userId = auth.currentUser?.uid
        userId?.let {
            firestore.collection("users").document(it)
                .collection("bookmarks").document(recipe.id.toString())
                .delete()
                .addOnSuccessListener {
                    Log.d(TAG, "Recipe removed from bookmarks")
                    updateRecipeInPredictionList(recipe.id!!, false)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error removing recipe from bookmarks", e)
                }
        } ?: run {
            Log.e(TAG, "Current user is null")
        }
    }

    private fun updateRecipeInPredictionList(recipeId: Int, isBookmarked: Boolean) {
        _recipePrediction.value?.let { list ->
            val updatedList = list.map {
                if (it.id == recipeId) {
                    it.copy(isBookmarked = isBookmarked)
                } else {
                    it
                }
            }
            _recipePrediction.value = updatedList
        }
    }

    private fun checkBookmarks() {
        val userId = auth.currentUser?.uid
        userId?.let {
            firestore.collection("users").document(it)
                .collection("bookmarks").get()
                .addOnSuccessListener { result ->
                    val bookmarkedIds = result.map { it.id.toInt() }
                    _recipePrediction.value?.let { list ->
                        val updatedList = list.map { recipe ->
                            if (recipe.id in bookmarkedIds) {
                                recipe.copy(isBookmarked = true)
                            } else {
                                recipe
                            }
                        }
                        _recipePrediction.value = updatedList
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error fetching bookmarks", e)
                }
        }
    }

    companion object {
        private const val TAG = "RecipeViewModel"
    }

    fun searchRecipe(query: String) {
        isSearching.value = true
        val client = ApiConfig.getApiPredictService().searchRecipe(query)
        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _recipePrediction.value = responseBody.data
                        checkBookmarks()
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun emptyRecipe(){
        _recipePrediction.value = emptyList()
    }
}
