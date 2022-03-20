package com.example.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import androidx.room.Room
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import com.example.notes.data.NotesDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  lateinit var db: NotesDB
  lateinit var noteDao: NoteDao
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    /// DATABASE INITIALIZATION
    db = Room.databaseBuilder(
            applicationContext,
            NotesDB::class.java, "notes-database"
          ).build()
    noteDao = db.noteDao()

    /// FAB button
    val fab = findViewById<FloatingActionButton>(R.id.new_note_fab)
    fab.setOnClickListener{
      val intent = Intent(this, EditNote::class.java)
      startActivity(intent)
    }


  }

  override fun onResume() {
    super.onResume()

    CoroutineScope(Dispatchers.IO).launch {
      val notes: List<Note> = noteDao.getAll()
      runOnUiThread {
        setNoteList(notes)
      }
    }
  }

  private fun setNoteList(notes: List<Note>) {
    val notesRecyclerView = findViewById<RecyclerView>(R.id.notes_recycle_view)
    notesRecyclerView.adapter = NotesAdapter(notes)
  }
}