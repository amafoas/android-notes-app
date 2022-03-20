package com.example.notes.data

import androidx.room.*

@Dao
interface NoteDao {

  @Query("SELECT * FROM Note")
  fun getAll(): List<Note>

  @Query("SELECT * FROM Note WHERE id = :id")
  fun getById(id: Int): Note

  @Update
  fun update(note: Note)

  @Insert
  fun insert(note: Note)

  @Delete
  fun delete(note: Note)
}