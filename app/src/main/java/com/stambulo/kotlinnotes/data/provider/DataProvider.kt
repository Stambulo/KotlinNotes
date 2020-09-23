package com.stambulo.kotlinnotes.data.provider

import androidx.lifecycle.LiveData
import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.data.model.NoteResult

interface DataProvider {
    fun subscribeToAllNotes() : LiveData<NoteResult>
    fun saveNote(note: Note) : LiveData<NoteResult>
    fun getNoteById(id: String) : LiveData<NoteResult>
}