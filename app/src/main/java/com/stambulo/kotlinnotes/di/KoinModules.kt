package com.stambulo.kotlinnotes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

import com.stambulo.kotlinnotes.data.NotesRepository
import com.stambulo.kotlinnotes.data.provider.DataProvider
import com.stambulo.kotlinnotes.data.provider.FirestoreProvider
import com.stambulo.kotlinnotes.ui.main.MainViewModel
import com.stambulo.kotlinnotes.ui.note.NoteViewModel
import com.stambulo.kotlinnotes.ui.splash.SplashViewModel

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single<DataProvider> { FirestoreProvider(get(), get()) }
    single { NotesRepository(get()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}