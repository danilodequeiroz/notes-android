package com.danilodequeiroz.login

import com.danilodequeiroz.notes_persistence.LoginData
import com.danilodequeiroz.notes_persistence.NotesDatabase

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
 * Test the implementation of [LoginDataDao]
 */
@RunWith(AndroidJUnit4::class)
class LoginDataDaoTest {
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
        database.loginDao().getLogin("some@email.com")
            .test()
            .assertNoValues()
    }


    @Test
    fun insertAndGetAllNote() {
        // When inserting a new Email data in the data source
        database.loginDao().insertLogin(EMAIL_1).blockingGet()
        database.loginDao().insertLogin(EMAIL_2).blockingGet()

        // When subscribing to the emissions of the data
        database.loginDao().getAllLogins()
            .test()
            // assertValue asserts that there was only one emission emails and pass
            .assertValue {
                it.first().email == EMAIL_1.email
                it.first().password == EMAIL_1.password

                it.last().email == EMAIL_2.email
                it.last().password == EMAIL_2.password
            }
    }

    @Test
    fun insertAndGetNote() {
        // When inserting a new login data in the data source
        database.loginDao().insertLogin(EMAIL_1).blockingGet()

        // When subscribing to the emissions of the login data
        database.loginDao().getLogin("valid1@email.com")
            .test()
            // assertValue asserts that there was only one emission of email and passwords
            .assertValue {
                it.email == "valid1@email.com" &&
                        it.password == "strong_password"
            }
    }

    @Test
    fun updateAndGetNote() {
        // Given that we have a login data in the data source
        database.loginDao().insertLogin(EMAIL_1).blockingGet()

        // When we are updating the name of the login data
        val updatedNote = EMAIL_2
        database.loginDao().insertLogin(updatedNote).blockingGet()

        // When subscribing to the emissions of the login data
        database.loginDao().getLogin("valid2@email.com")
            .test()
            // assertValue asserts that there was only one emission of the login data
            .assertValue {
                it.email == "valid2@email.com" &&
                        it.password == "stronger_password"
            }
    }

    @Test
    fun deleteAndGetNote() {
        // Given that we have a login data in the data source
        database.loginDao().insertLogin(EMAIL_2).blockingGet()


        //When we are deleting all login datas
        database.loginDao().deleteLogin("valid2@email.com")
        // When subscribing to the emissions of the login data
        database.loginDao().getLogin("valid2@email.com")
            .test()
            // check that there's no login data emitted
            .assertNoValues()
    }


    companion object {
        private val EMAIL_1 = LoginData("valid1@email.com", "strong_password")
        private val EMAIL_2 = LoginData("valid2@email.com", "stronger_password")
    }
}
