package com.danilodequeiroz.notes.domain.notepersistence


import io.reactivex.Single

interface DeleteAllNotesUseCase {
    fun deleteAllNotes(): Single<Unit>
}