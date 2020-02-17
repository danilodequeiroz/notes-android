package com.danilodequeiroz.notes.domain.notepersistence

import com.danilodequeiroz.notes_persistence.Note
import com.danilodequeiroz.notes_persistence.NotesRepository

import io.reactivex.Single


class UpdateNoteUseCaseImpl(val repository: NotesRepository) :
    UpdateNoteUseCase {
    override fun updateNote(
        noteId: Long?,
        title: String,
        description: String,
        priority: Int
    ): Single<Long> {
        return repository.insertNote(
                if (noteId == null) {
                    Note(
                        id = null,
                        title = title,
                        description = description,
                        priority = priority
                    )
                } else {
                    Note(
                        id = noteId,
                        title = title,
                        description = description,
                        priority = priority
                    )
                }
            )

    }

}