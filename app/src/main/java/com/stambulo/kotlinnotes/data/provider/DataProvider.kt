package com.stambulo.kotlinnotes.data.provider

import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.data.entity.User
import com.stambulo.kotlinnotes.data.model.NoteResult
import kotlinx.coroutines.channels.ReceiveChannel

interface DataProvider {
    fun subscribeToAllNotes() : ReceiveChannel<NoteResult>

    suspend fun getCurrentUser(): User?
    suspend fun saveNote(note: Note) : Note
    suspend fun getNoteById(id: String) : Note?
    suspend fun deleteNote(id: String)
}