package com.danilodequeiroz.notes_persistence

import io.reactivex.Flowable
import io.reactivex.Single


interface NotesRepository {

    fun getNoteById(id: Long): Flowable<Note>
    fun getAllNotes(): Flowable<MutableList<Note>>
    fun insertNote(note: Note): Single<Long>
    fun deleteNoteById(id: Long): Single<Unit>
    fun deleteAllNotes(): Single<Unit>

}