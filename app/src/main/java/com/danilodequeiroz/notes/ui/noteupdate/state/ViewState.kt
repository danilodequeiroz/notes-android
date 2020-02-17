package com.danilodequeiroz.notes.ui.noteupdate.state

import androidx.annotation.StringRes
import com.danilodequeiroz.notes.ui.item.NoteItemViewModel

sealed class ViewState {
    abstract val noteId: Long?
}

data class LoadingState(override val noteId: Long? = null) : ViewState()
data class SuccessUpdateState(override val noteId: Long) : ViewState()
data class ErrorState(
    @StringRes val errorMessage: Int,
    override val noteId: Long? = null
) : ViewState()