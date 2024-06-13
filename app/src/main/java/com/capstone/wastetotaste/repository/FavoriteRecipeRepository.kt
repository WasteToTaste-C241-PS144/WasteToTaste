package com.capstone.wastetotaste.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.capstone.wastetotaste.database.AppRoomDatabase
import com.capstone.wastetotaste.database.FavoriteRecipe
import com.capstone.wastetotaste.database.FavoriteRecipeDAO
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRecipeRepository(application: Application) {
//    private val mFavoriteRecipeDAO: FavoriteRecipeDAO
//    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
//    init {
//        val db = AppRoomDatabase.getDatabase(application)
//        mFavoriteRecipeDAO = db.favoriteRecipeDAO()
//    }
//
//    fun insert(favoriteRecipe: FavoriteRecipe) {
//        executorService.execute { mFavoriteRecipeDAO.insert(favoriteRecipe)}
//    }
//
//    fun delete(favoriteRecipe: FavoriteRecipe) {
//        executorService.execute { mFavoriteRecipeDAO.delete(favoriteRecipe) }
//    }
//
//    fun getAllFavoriteRecipe(): LiveData<List<FavoriteRecipe>> = mFavoriteRecipeDAO.getAllFavoriteRecipe()
}