package com.danilodequeiroz.notes.domain.notepersistence

import io.reactivex.Single

interface DeleteNoteByIdUseCase {
    fun deleteNoteById(noteId: Long): Single<Unit>
}