package com.danilodequeiroz.notes.ui.notelist.state

import androidx.annotation.StringRes
import com.danilodequeiroz.notes.ui.item.NoteItemViewModel

sealed class ViewState {
    abstract val data: MutableList<NoteItemViewModel>?
}
//@formatter:off
data class LoadingState(override val data: MutableList<NoteItemViewModel>? = null) : ViewState()
data class EmptyState(override val data: MutableList<NoteItemViewModel>? = null) : ViewState()
data class SuccessState(override val data: MutableList<NoteItemViewModel>) : ViewState()
data class UpdatedItemState(override val data: MutableList<NoteItemViewModel>? = null) : ViewState()
data class DeletedItemState(val noteId:Long, override val data: MutableList<NoteItemViewModel>? = null) : ViewState()
data class ErrorState(
    @StringRes val errorMessage: Int,
    override val data: MutableList<NoteItemViewModel>? = null
) : ViewState()
//@formatter:on