package com.stambulo.kotlinnotes.ui.note

import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.ui.base.BaseViewState

class NoteViewState(note: Note? = null, error: Throwable? = null) : BaseViewState<Note?>(note, error)