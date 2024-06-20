package com.capstone.wastetotaste.ui.home

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.wastetotaste.api.ApiConfig
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.data.RecipeResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class RecipeResultViewModel(application: Application) : AndroidViewModel(application) {
    val _searchResult = MutableLiveData<List<Recipe>>()
    val searchResult: LiveData<List<Recipe>> get() = _searchResult
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private fun showToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
    fun addToBookmarks(recipe: Recipe) {
        val userId = auth.currentUser?.uid
        userId?.let {
            firestore.collection("users").document(it)
                .collection("bookmarks").document(recipe.id.toString())
                .set(recipe)
                .addOnSuccessListener {
                    showToast("Resep berhasil dibookmark")
                    updateRecipeInPredictionList(recipe.id!!, true)
                }
                .addOnFailureListener { e ->
                    showToast("Resep gagal dibookmark: ${e.message}")
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
                    showToast("Resep berhasil dihapus dari bookmark")
                    updateRecipeInPredictionList(recipe.id!!, false)
                }
                .addOnFailureListener { e ->
                    showToast("Resep gagal dihapus dari bookmark: ${e.message}")
                }
        } ?: run {
            Log.e(TAG, "Current user is null")
        }
    }
     fun updateRecipeInPredictionList(recipeId: Int, isBookmarked: Boolean) {
        _searchResult.value?.let { list ->
            val updatedList = list.map {
                if (it.id == recipeId) {
                    it.copy(isBookmarked = isBookmarked)
                } else {
                    it
                }
            }
            _searchResult.value = updatedList
        }
    }

    private fun checkBookmarks() {
        val userId = auth.currentUser?.uid
        userId?.let {
            firestore.collection("users").document(it)
                .collection("bookmarks").get()
                .addOnSuccessListener { result ->
                    val bookmarkedIds = result.map { it.id.toInt() }
                    _searchResult.value?.let { list ->
                        val updatedList = list.map { recipe ->
                            if (recipe.id in bookmarkedIds) {
                                recipe.copy(isBookmarked = true)
                            } else {
                                recipe
                            }
                        }
                        _searchResult.value = updatedList
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
        val client = ApiConfig.getApiPredictService().searchRecipe(query)
        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _searchResult.value = responseBody.data
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
        _searchResult.value = emptyList()
    }
}