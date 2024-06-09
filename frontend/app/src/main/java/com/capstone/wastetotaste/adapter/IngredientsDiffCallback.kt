package com.capstone.wastetotaste.adapter

import androidx.recyclerview.widget.DiffUtil
import com.capstone.wastetotaste.database.Ingredients

class IngredientsDiffCallback(private val oldNoteList: List<Ingredients>, private val newNoteList: List<Ingredients>) : DiffUtil.Callback(){
    override fun getOldListSize(): Int = oldNoteList.size
    override fun getNewListSize(): Int = newNoteList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNoteList[oldItemPosition].id == newNoteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldNoteList[oldItemPosition]
        val newNote = newNoteList[newItemPosition]
        return oldNote.name == newNote.name && oldNote.expiryDate == newNote.expiryDate
    }
}