package com.stambulo.kotlinnotes.data

import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.data.provider.DataProvider
import com.stambulo.kotlinnotes.data.provider.FirestoreProvider

object NotesRepository {

    private val dataProvider: DataProvider = FirestoreProvider()

    fun getCurrentUser() = dataProvider.getCurrentUser()
    fun getNotes() = dataProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)

}