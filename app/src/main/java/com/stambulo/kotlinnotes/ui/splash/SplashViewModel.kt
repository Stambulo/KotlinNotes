package com.stambulo.kotlinnotes.ui.splash

import com.stambulo.kotlinnotes.data.NotesRepository
import com.stambulo.kotlinnotes.data.errors.NoAuthException
import com.stambulo.kotlinnotes.ui.base.BaseViewModel

class SplashViewModel() : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser(){
        NotesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = if(it != null){
                SplashViewState(authenticated = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }

}