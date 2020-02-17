package com.danilodequeiroz.notes.domain.notepersistence

import com.danilodequeiroz.notes.ui.item.NoteItemViewModel
import com.danilodequeiroz.notes_persistence.NotesRepository

import io.reactivex.Flowable


class GetNoteListUseCaseImpl(val repository: NotesRepository) :
    GetNoteListUseCase {

    override fun getAllNotes(): Flowable<MutableList<NoteItemViewModel>> {
        return repository.getAllNotes().map { notes ->
            notes.map { note ->
                NoteItemViewModel(
                    note.id,
                    note.title,
                    note.description,
                    note.priority
                )
            }.toMutableList()
        }
    }
}