package com.capstone.wastetotaste.ui.bookmark

// BookmarkViewModel.kt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.wastetotaste.data.Recipe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookmarkViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
//    private val recipesCollection = firestore.collection("recipes")
    private val auth = FirebaseAuth.getInstance()


    private val _bookmarkRecipes = MutableLiveData<List<Recipe>>()
    val bookmarkRecipes: LiveData<List<Recipe>>
        get() = _bookmarkRecipes

    init {
        loadBookmarkRecipes()
    }

    private fun loadBookmarkRecipes() {
        val user = auth.currentUser
        if (user != null) {
            val userId = user.uid
            val userBookmarksCollection = firestore.collection("users").document(userId).collection("bookmarks")

            userBookmarksCollection.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val recipesList = mutableListOf<Recipe>()
                snapshot?.documents?.forEach { document ->
                    val recipe = document.toObject(Recipe::class.java)
                    recipe?.let {
                        recipesList.add(it)
                    }
                }

                _bookmarkRecipes.value = recipesList
            }
        }
    }
}
