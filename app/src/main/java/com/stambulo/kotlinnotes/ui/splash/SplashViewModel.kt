package com.stambulo.kotlinnotes.ui.splash

import com.stambulo.kotlinnotes.data.NotesRepository
import com.stambulo.kotlinnotes.data.errors.NoAuthException
import com.stambulo.kotlinnotes.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(val notesRepository: NotesRepository) : BaseViewModel<Boolean?>() {

    fun requestUser() = launch {
        notesRepository.getCurrentUser()?.let {
            setData(true)
        } ?: setError(NoAuthException())
    }

}