package com.danilodequeiroz.notes.domain.notepersistence

import com.danilodequeiroz.notes_persistence.NotesRepository

import io.reactivex.Single


class DeleteNoteByIdUseCaseImpl(val repository: NotesRepository) :
    DeleteNoteByIdUseCase {
    override fun deleteNoteById(noteId: Long): Single<Unit> {
        return repository.deleteNoteById(noteId)
    }
}