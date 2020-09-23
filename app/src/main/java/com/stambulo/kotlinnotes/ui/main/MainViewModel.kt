package com.stambulo.kotlinnotes.ui.main

import androidx.lifecycle.Observer
import com.stambulo.kotlinnotes.data.NotesRepository
import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.data.model.NoteResult
import com.stambulo.kotlinnotes.ui.base.BaseViewModel

class MainViewModel() : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer<NoteResult> { result ->
        result ?: return@Observer
        when(result){
            is NoteResult.Success<*> ->  viewStateLiveData.value = MainViewState(notes = result.data as? List<Note>)
            is NoteResult.Error -> viewStateLiveData.value = MainViewState(error = result.error)
        }
    }

    private val repositoryNotes = NotesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        super.onCleared()
        repositoryNotes.removeObserver(notesObserver)
    }
}