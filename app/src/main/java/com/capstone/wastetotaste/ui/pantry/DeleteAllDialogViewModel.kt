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

class DeleteAllDialogViewModel(application: Application) : ViewModel() {
    private val mIngredientsRepository: IngredientsRepository = IngredientsRepository(application)
    fun deleteAllIngredients() {
        mIngredientsRepository.deleteAllIngredients()
    }

}
