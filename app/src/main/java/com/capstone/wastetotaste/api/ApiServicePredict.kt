package com.capstone.wastetotaste.api
import com.capstone.wastetotaste.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
interface ApiServicePredict {
    @POST("predict")
    fun predictRecipe(
        @Body request: IngredientsRequest
    ): Call<RecipeResponse>

    @GET("recipes")
    fun searchRecipe(
        @Query("search") query: String
    ): Call<RecipeResponse>
}
