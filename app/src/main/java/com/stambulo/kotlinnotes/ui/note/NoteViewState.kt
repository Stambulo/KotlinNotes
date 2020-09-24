package com.stambulo.kotlinnotes.ui.note

import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) : BaseViewState<NoteViewState.Data>(data, error) {
    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}