package com.danilodequeiroz.notes.domain.notepersistence

import com.danilodequeiroz.notes.ui.item.NoteItemViewModel
import io.reactivex.Flowable

interface GetNoteByIdUseCase {
    fun getNoteById(noteId:Long): Flowable<NoteItemViewModel>
}