package com.capstone.wastetotaste.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface IngredientsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ingredients: Ingredients): Long
    @Update
    fun update(ingredients: Ingredients)
    @Delete
    fun delete(ingredients: Ingredients)
    @Query("SELECT * FROM ingredients WHERE expiry_date > :oneMonthLater ORDER BY id ASC")
    fun getAllSafeIngredients(oneMonthLater: Long): LiveData<List<Ingredients>>

    @Query("SELECT * FROM ingredients ORDER BY id ASC")
    fun getAllIngredients(): LiveData<List<Ingredients>>

    @Query("""
        SELECT * FROM ingredients 
        WHERE expiry_date > :currentDate
        AND expiry_date <= :oneMonthLater
        ORDER BY id ASC
    """)
    fun getAllNearlyExpiredIngredients(currentDate: Long, oneMonthLater: Long): LiveData<List<Ingredients>>

    @Query("SELECT * FROM ingredients WHERE expiry_date < :currentDate ORDER BY id ASC")
    fun getAllExpiredIngredients(currentDate: Long): LiveData<List<Ingredients>>

    @Query("SELECT * FROM ingredients WHERE name = :name LIMIT 1")
    fun getIngredientByName(name: String): Ingredients?
}