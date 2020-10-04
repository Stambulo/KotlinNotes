package com.stambulo.kotlinnotes.ui.main

import androidx.annotation.VisibleForTesting
import com.stambulo.kotlinnotes.data.NotesRepository
import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.data.model.NoteResult
import com.stambulo.kotlinnotes.ui.base.BaseViewModel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class MainViewModel(notesRepository: NotesRepository) : BaseViewModel<List<Note>?>() {

    private val notesChannel = notesRepository.getNotes()

    init {
        launch {
            notesChannel.consumeEach {
                when(it){
                    is NoteResult.Success<*> ->  setData( it.data as? List<Note>)
                    is NoteResult.Error -> setError(it.error)
                }
            }
        }
    }

    @VisibleForTesting
    override public fun onCleared() {
        super.onCleared()
        notesChannel.cancel()
    }
}
