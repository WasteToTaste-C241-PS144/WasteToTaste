package com.capstone.wastetotaste.data.response

import com.google.gson.annotations.SerializedName

data class RecipeResponse(

	@field:SerializedName("number")
	val number: Int,

	@field:SerializedName("totalResults")
	val totalResults: Int,

	@field:SerializedName("offset")
	val offset: Int,

	@field:SerializedName("results")
	val results: ArrayList<ResultsItem>
)

data class ResultsItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("imageType")
	val imageType: String
)
