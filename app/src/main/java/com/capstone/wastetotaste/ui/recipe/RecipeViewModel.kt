package com.capstone.wastetotaste.ui.recipe

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
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

class RecipeViewModel(application: Application) : AndroidViewModel(application) {
    val _recipePrediction = MutableLiveData<List<Recipe>>()
    val recipePrediction: LiveData<List<Recipe>> get() = _recipePrediction
    private val mIngredientsRepository: IngredientsRepository = IngredientsRepository(application)
    val allIngredients: LiveData<List<String>> = mIngredientsRepository.getAllIngredientsName()
    val isSearching = MutableLiveData<Boolean>()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    init {
        observeBookmarks()
    }
    private fun showToast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
    fun setValue(value: ArrayList<Recipe>){
        _recipePrediction.value = value
    }

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

    private fun observeBookmarks() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            firestore.collection("users").document(userId).collection("bookmarks")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w("RecipeViewModel", "listen:error", e)
                        return@addSnapshotListener
                    }

                    val bookmarkedRecipes = mutableListOf<Recipe>()
                    for (doc in snapshots!!) {
                        val recipe = doc.toObject(Recipe::class.java)
                        bookmarkedRecipes.add(recipe)
                    }
                    updateBookmarkedRecipes(bookmarkedRecipes)
                }
        }
    }

    private fun updateBookmarkedRecipes(bookmarkedRecipes: List<Recipe>) {
        val currentRecipes = _recipePrediction.value?.toMutableList() ?: mutableListOf()
        for (recipe in currentRecipes) {
            recipe.isBookmarked = bookmarkedRecipes.any { it.id == recipe.id }
        }
        _recipePrediction.postValue(currentRecipes)
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
