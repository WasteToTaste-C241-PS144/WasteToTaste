package com.capstone.wastetotaste.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteRecipeDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteRecipe: FavoriteRecipe)
    @Delete
    fun delete(favoriteRecipe: FavoriteRecipe)
    @Query("SELECT * FROM favoriteRecipe")
    fun getAllFavoriteRecipe(): LiveData<List<FavoriteRecipe>>
}