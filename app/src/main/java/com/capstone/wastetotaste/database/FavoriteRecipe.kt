package com.capstone.wastetotaste.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class FavoriteRecipe (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "image")
    val image: String? = null,

    @ColumnInfo(name = "ingredients")
    val ingredients: String,

    @ColumnInfo(name = "instruction")
    val instruction: String
): Parcelable