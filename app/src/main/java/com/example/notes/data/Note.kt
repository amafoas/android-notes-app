package com.example.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
  var title: String,
  var color: Int,
  var content: String
){
  @PrimaryKey(autoGenerate = true)
  var id: Int = 0
}
