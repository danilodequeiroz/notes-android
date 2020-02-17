package com.danilodequeiroz.notes.domain.notepersistence

import com.danilodequeiroz.notes.ui.item.NoteItemViewModel
import com.danilodequeiroz.notes_persistence.NotesRepository

import io.reactivex.Flowable


class GetNoteByIdUseCaseImpl(val repository: NotesRepository) :
    GetNoteByIdUseCase {
    override fun getNoteById(noteId: Long): Flowable<NoteItemViewModel> {
        return repository.getNoteById(noteId).map { note ->
            NoteItemViewModel(
                note.id,
                note.title,
                note.description,
                note.priority
            )
        }
    }
}
