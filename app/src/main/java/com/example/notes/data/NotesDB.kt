package com.example.notes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [Note::class],
  version = 1
)
abstract class NotesDB : RoomDatabase() {
  abstract fun noteDao(): NoteDao

  companion object {
    private var INSTANCE: NotesDB? = null

    fun getDatabase(context: Context): NotesDB {
      val tempInstant = INSTANCE
      if (tempInstant != null) return tempInstant

      synchronized(this) {
        val instance = Room.databaseBuilder(context,
          NotesDB::class.java, "contact_database"
          ).build()
        INSTANCE = instance
        return instance
      }
    }
  }
}