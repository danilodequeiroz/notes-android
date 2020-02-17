package com.danilodequeiroz.notes_persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "noteid")
    var id: Long?,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "priority")
    val priority: Int
)