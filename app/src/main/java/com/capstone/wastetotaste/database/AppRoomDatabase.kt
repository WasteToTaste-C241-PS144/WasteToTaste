package com.capstone.wastetotaste.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(entities = [Ingredients::class], version = 1)

abstract class AppRoomDatabase: RoomDatabase() {
    abstract fun ingredientsDAO(): IngredientsDAO
    //abstract fun favoriteRecipeDAO(): FavoriteRecipeDAO

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): AppRoomDatabase {
            if (INSTANCE == null) {
                synchronized(AppRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppRoomDatabase::class.java, "waste_to_taste_database")
                        .build()
                }
            }
            return INSTANCE as AppRoomDatabase
        }
    }
}
