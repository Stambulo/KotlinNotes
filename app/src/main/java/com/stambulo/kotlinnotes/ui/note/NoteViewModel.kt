package com.stambulo.kotlinnotes.ui.note

import com.stambulo.kotlinnotes.data.NotesRepository
import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class NoteViewModel(val notesRepository: NotesRepository) : BaseViewModel<NoteData>() {

    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    fun loadNote(noteId: String) = launch {
        try {
            notesRepository.getNoteById(noteId)?.let {
                setData(NoteData(note = it))
            }
        } catch (e: Throwable) {
            setError(e)
        }
    }

    fun deleteNote() = launch {
        try {
            pendingNote?.let { notesRepository.deleteNote(it.id) }
            setData(NoteData(isDeleted = true))

        } catch (e: Throwable) {
            setError(e)
        }
    }

    override fun onCleared() {
        launch {
            pendingNote?.let {
                notesRepository.saveNote(it)
            }
        }
    }

}