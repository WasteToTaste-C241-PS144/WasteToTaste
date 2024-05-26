package com.capstone.wastetotaste.data.retrofit

import com.capstone.wastetotaste.BuildConfig
import com.capstone.wastetotaste.data.response.RecipeResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("complexSearch")
    fun searchRecipe(
        @Query("query") query: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Call<RecipeResponse>
}