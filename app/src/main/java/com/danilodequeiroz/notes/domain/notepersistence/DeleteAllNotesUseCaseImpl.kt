package com.danilodequeiroz.notes.domain.notepersistence


import com.danilodequeiroz.notes_persistence.NotesRepository

import io.reactivex.Single


class DeleteAllNotesUseCaseImpl(val repository: NotesRepository) :
    DeleteAllNotesUseCase {
    override fun deleteAllNotes(): Single<Unit> {
        return repository.deleteAllNotes()
    }
}