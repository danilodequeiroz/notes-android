package com.danilodequeiroz.notes.domain.notepersistence

import io.reactivex.Single


interface UpdateNoteUseCase {
    fun updateNote(noteId: Long? = null,
                   title: String,
                   description: String,
                   priority: Int): Single<Long>
}