package com.capstone.wastetotaste.ui.home

import android.app.Application
import android.content.ContentValues
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(application: Application) : ViewModel() {
    private val mIngredientsRepository: IngredientsRepository = IngredientsRepository(application)
    val allIngredients: LiveData<List<String>> = mIngredientsRepository.getAllIngredientsName()
    private val _recipePrediction = MutableLiveData<List<Recipe>>()
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
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
    fun emptyRecipe(){
        _recipePrediction.value = emptyList()
    }
}