package com.danilodequeiroz.notes_persistence


import io.reactivex.Flowable
import io.reactivex.Single


class NotesRepositoryImpl(val noteDao: NoteDao) : NotesRepository {

    override fun getNoteById(id: Long): Flowable<Note> {
        return noteDao.getNoteById(id)
    }

    override fun getAllNotes(): Flowable<MutableList<Note>> {
        return noteDao.getAllNotes()
    }

    override fun insertNote(note: Note): Single<Long> {
        return noteDao.insertNote(note)
    }

    override fun deleteNoteById(id: Long): Single<Unit> {
        return Single.fromCallable { (noteDao.deleteNoteById(id)) }
    }

    override fun deleteAllNotes(): Single<Unit> {
        return Single.fromCallable {  noteDao.deleteAllNotes()}
    }

}