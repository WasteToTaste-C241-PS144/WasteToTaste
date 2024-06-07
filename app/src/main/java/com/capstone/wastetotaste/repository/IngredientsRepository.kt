package com.capstone.wastetotaste.repository

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.capstone.wastetotaste.database.AppRoomDatabase
import com.capstone.wastetotaste.database.Ingredients
import com.capstone.wastetotaste.database.IngredientsDAO
import java.util.Calendar
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class IngredientsRepository(application: Application) {
    private val mIngredientsDAO: IngredientsDAO
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = AppRoomDatabase.getDatabase(application)
        mIngredientsDAO = db.ingredientsDAO()
    }
    fun getAllSafeIngredients(): LiveData<List<Ingredients>> {
        val oneMonthLaterDate = getOneMonthLaterDate()
        return mIngredientsDAO.getAllSafeIngredients(oneMonthLaterDate)
    }

    fun getAllNearlyExpiredIngredients(): LiveData<List<Ingredients>> {
        val currentDate = getCurrentDate()
        val oneMonthLaterDate = getOneMonthLaterDate()
        return mIngredientsDAO.getAllNearlyExpiredIngredients(currentDate, oneMonthLaterDate)
    }

    fun getAllExpiredIngredients(): LiveData<List<Ingredients>> {
        val currentDate = getCurrentDate()
        return mIngredientsDAO.getAllExpiredIngredients(currentDate)
    }

//     fun insert(ingredients: Ingredients, callback: (Boolean) -> Unit) {
//         executorService.execute {
//             val existingIngredient = mIngredientsDAO.getIngredientByName(ingredients.name)
//             val isUnique = existingIngredient == null
//
//             if (isUnique) {
//                 mIngredientsDAO.insert(ingredients)
//             }
//             callback(isUnique)
//         }
//    }

//     fun insert(ingredients: Ingredients) {
////        return if (mIngredientsDAO.getIngredientByName(ingredients.name) == null) {
//         mIngredientsDAO.insert(ingredients)
////            true
////        } else {
////            false
////        }
//    }

    fun insert(ingredient: Ingredients, onResult: (Boolean) -> Unit) {
        executorService.execute {
            try {
                val result = mIngredientsDAO.insert(ingredient)
                onResult(result != -1L) // Success if result is not -1
            } catch (e: SQLiteConstraintException) {
                onResult(false) // Failure due to constraint violation
            }
        }
    }



    fun delete(ingredients: Ingredients) {
        executorService.execute { mIngredientsDAO.delete(ingredients) }
    }
    fun update(ingredients: Ingredients) {
        executorService.execute { mIngredientsDAO.update(ingredients) }
    }

    fun getCurrentDate(): Long {
        return Calendar.getInstance().timeInMillis
    }

    fun getOneMonthLaterDate(): Long {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        return calendar.timeInMillis
    }

    fun getAllIngredients(): LiveData<List<Ingredients>> = mIngredientsDAO.getAllIngredients()

    fun getIngredientByName(name: String): Ingredients? {
        return mIngredientsDAO.getIngredientByName(name)
    }

}