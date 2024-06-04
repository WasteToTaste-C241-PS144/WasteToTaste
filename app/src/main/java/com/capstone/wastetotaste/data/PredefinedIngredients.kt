package com.capstone.wastetotaste.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

data class PredefinedIngredients(val name: String)
fun loadIngredients(context: Context): List<PredefinedIngredients> {
    val inputStream = context.assets.open("ingredients.json")
    val reader = InputStreamReader(inputStream)
    val ingredientType = object : TypeToken<List<PredefinedIngredients>>() {}.type
    return Gson().fromJson(reader, ingredientType)
}
