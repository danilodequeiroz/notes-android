package com.danilodequeiroz.notes_persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface NoteDao {

    /**
     * Get all notes by id.
     * @return all notes.
     */
    @Query("SELECT * FROM Notes")
    fun getAllNotes(): Flowable<MutableList<Note>>

    /**
     * Get a note by id.
     * @return the note from the table with a specific id.
     */
    @Query("SELECT * FROM Notes WHERE noteid = :id")
    fun getNoteById(id: Long): Flowable<Note>

    /**
     * Insert a note in the database. If the note already exists, replace it.
     * @param note the note to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note): Single<Long>

    /**
     * Dele a note in the database.
     * @param id the note id to be deleted.
     */
    @Query("DELETE FROM Notes WHERE noteid = :id")
    fun deleteNoteById(id: Long)

    /**
     * Delete all notes.
     */
    @Query("DELETE FROM notes")
    fun deleteAllNotes()
}