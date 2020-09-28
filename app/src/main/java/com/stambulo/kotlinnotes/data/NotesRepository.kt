package com.stambulo.kotlinnotes.data

import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.data.provider.DataProvider

class NotesRepository(val dataProvider: DataProvider) {
    fun getCurrentUser() = dataProvider.getCurrentUser()
    fun getNotes() = dataProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = dataProvider.saveNote(note)
    fun getNoteById(id: String) = dataProvider.getNoteById(id)
    fun deleteNote(id: String) = dataProvider.deleteNote(id)
}