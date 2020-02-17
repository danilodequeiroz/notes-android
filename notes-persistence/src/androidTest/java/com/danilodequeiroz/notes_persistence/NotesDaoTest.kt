package com.danilodequeiroz.notes_persistence

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import org.junit.Rule

/**
 * Test the implementation of [NoteDao]
 */
@RunWith(AndroidJUnit4::class)
class NotesDaoTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: NotesDatabase

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears after test
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesDatabase::class.java
        )
            // allowing main thread queries, just for testing
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getNotesWhenNoNoteInserted() {
        database.noteDao().getNoteById(1)
            .test()
            .assertNoValues()
    }


    @Test
    fun insertAndGetAllNote() {
        // When inserting a new note in the data source
        database.noteDao().insertNote(NOTE).blockingGet()
        database.noteDao().insertNote(NOTE).blockingGet()

        // When subscribing to the emissions of the note
        database.noteDao().getAllNotes()
            .test()
            // assertValue asserts that there was only one emission of the note
            .assertValue {
                it.first().id == 1L
                it.first().title == NOTE.title
                it.first().description == NOTE.description
                it.first().priority == NOTE.priority

                it.last().id == 2L
                it.last().title == NOTE.title
                it.last().description == NOTE.description
                it.last().priority == NOTE.priority
            }
    }

    @Test
    fun insertAndGetNote() {
        // When inserting a new note in the data source
        database.noteDao().insertNote(NOTE).blockingGet()

        // When subscribing to the emissions of the note
        database.noteDao().getNoteById(1L)
            .test()
            // assertValue asserts that there was only one emission of the note
            .assertValue {
                it.id == 1L &&
                        it.title == NOTE.title &&
                        it.description == NOTE.description &&
                        it.priority == NOTE.priority
            }
    }

    @Test
    fun updateAndGetNote() {
        // Given that we have a note in the data source
        database.noteDao().insertNote(NOTE).blockingGet()

        // When we are updating the name of the note
        val updatedNote = Note( 1L,"new note", "some description", 3)
        database.noteDao().insertNote(updatedNote).blockingGet()

        // When subscribing to the emissions of the note
        database.noteDao().getNoteById(1L)
            .test()
            // assertValue asserts that there was only one emission of the note
            .assertValue {
                it.id == 1L &&
                        it.title == "new note" &&
                        it.description == "some description" &&
                        it.priority == 3
            }
    }

    @Test
    fun deleteAllAndGetNote() {
        // Given that we have a note in the data source
        database.noteDao().insertNote(NOTE).blockingGet()


        //When we are deleting all notes
        database.noteDao().deleteAllNotes()
        // When subscribing to the emissions of the note
        database.noteDao().getNoteById(1L)
            .test()
            // check that there's no note emitted
            .assertNoValues()
    }

    @Test
    fun deleteByIdAndGetNote() {
        // Given that we have a note in the data source
        database.noteDao().insertNote(NOTE).blockingGet()

        //When we are deleting all notes
        database.noteDao().deleteNoteById(1L)
        // When subscribing to the emissions of the note
        database.noteDao().getNoteById(1L)
            .test()
            // check that there's no note emitted
            .assertNoValues()
    }

    companion object {
        private val NOTE = Note(
            null,
            "note",
            "desc",
            1
        )
    }
}
