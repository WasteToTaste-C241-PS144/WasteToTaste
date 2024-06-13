package com.capstone.wastetotaste.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.wastetotaste.database.Ingredients
import com.capstone.wastetotaste.repository.IngredientsRepository

class HomeViewModel(application: Application) : ViewModel() {
    private val mIngredientsRepository: IngredientsRepository = IngredientsRepository(application)
    val allIngredients: LiveData<List<Ingredients>> = mIngredientsRepository.getAllIngredients()
}