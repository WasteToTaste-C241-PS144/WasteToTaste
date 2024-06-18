package com.capstone.wastetotaste.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class RecipeResponse(

	@field:SerializedName("data")
	val data: List<Recipe> = emptyList(),

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class Recipe(

	@field:SerializedName("imgUrl")
	val imgUrl: String? = null,

	@field:SerializedName("ingredients")
	val ingredients: List<String?>? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("steps")
	val steps: List<String?>? = null,

	var isBookmarked: Boolean = false  // tambahkan atribut ini

): Parcelable
