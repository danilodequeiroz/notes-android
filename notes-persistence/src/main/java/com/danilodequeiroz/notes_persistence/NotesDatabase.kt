package com.danilodequeiroz.notes_persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class, LoginData::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
    abstract fun loginDao(): LoginDataDao

    companion object {

        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NotesDatabase::class.java, "Notes.db"
            )
                .build()
    }
}