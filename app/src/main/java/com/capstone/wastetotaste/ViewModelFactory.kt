package com.capstone.wastetotaste

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.wastetotaste.ui.home.HomeViewModel
import com.capstone.wastetotaste.ui.pantry.DeleteAllDialogViewModel
import com.capstone.wastetotaste.ui.pantry.PantryViewModel

class ViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PantryViewModel::class.java) -> PantryViewModel(mApplication) as T
            modelClass.isAssignableFrom(DeleteAllDialogViewModel::class.java) -> DeleteAllDialogViewModel(mApplication) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(mApplication) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}