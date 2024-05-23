package com.capstone.wastetotaste

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val name: String,
    val ingredients: ArrayList<String>,
    val photo: String,
    val instruction: String
): Parcelable
