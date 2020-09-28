package com.stambulo.kotlinnotes.ui.splash

import com.stambulo.kotlinnotes.ui.base.BaseViewState

class SplashViewState(authenticated: Boolean? = null, error: Throwable? = null): BaseViewState<Boolean?>(authenticated, error)