package com.capstone.wastetotaste.ui.pantry

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.capstone.wastetotaste.database.Ingredients
import com.capstone.wastetotaste.repository.IngredientsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PantryViewModel(application: Application) : ViewModel() {
    private val mIngredientsRepository: IngredientsRepository = IngredientsRepository(application)
    val allIngredients: LiveData<List<Ingredients>> = mIngredientsRepository.getAllIngredients()


    fun insert(name: String, onResult: (Boolean) -> Unit) {
        val ingredient = Ingredients(name = name, expiryDate = null)
        mIngredientsRepository.insert(ingredient, onResult)
    }

    fun delete(ingredient: Ingredients) {
        mIngredientsRepository.delete(ingredient)
    }

    fun deleteAllIngredients() {
        mIngredientsRepository.deleteAllIngredients()
    }


}

