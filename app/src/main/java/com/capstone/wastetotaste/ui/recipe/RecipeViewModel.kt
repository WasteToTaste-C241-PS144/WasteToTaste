package com.capstone.wastetotaste.ui.recipe

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.wastetotaste.api.ApiConfig
import com.capstone.wastetotaste.api.IngredientsRequest
import com.capstone.wastetotaste.data.Recipe
import com.capstone.wastetotaste.data.RecipeResponse
import com.capstone.wastetotaste.database.Ingredients
import com.capstone.wastetotaste.repository.IngredientsRepository
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class RecipeViewModel(application: Application) : ViewModel() {
    private val _recipePrediction = MutableLiveData<List<Recipe>>()
    private val mIngredientsRepository: IngredientsRepository = IngredientsRepository(application)
    val allIngredients: LiveData<List<String>> = mIngredientsRepository.getAllIngredientsName()
    val recipePrediction: LiveData<List<Recipe>> get() = _recipePrediction
    fun predictRecipe(request: IngredientsRequest) {
        val client = ApiConfig.getApiPredictService().predictRecipe(request)
        client.enqueue(object : Callback<RecipeResponse> {
            override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                if (response.isSuccessful) {
                   val responseBody = response.body()
                    if (responseBody != null) {
                        _recipePrediction.value = responseBody.data
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
}