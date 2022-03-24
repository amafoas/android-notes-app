package com.example.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
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
  lateinit var adapter: RecyclerView.Adapter<*>
  lateinit var noteList: MutableList<Note>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    /// DATABASE INITIALIZATION
    db = Room.databaseBuilder(
            applicationContext,
            NotesDB::class.java, "notes-database"
          ).build()
    noteDao = db.noteDao()

    val notesRecyclerView = findViewById<RecyclerView>(R.id.notes_recycle_view)
    noteList = mutableListOf()
    adapter = NotesAdapter(noteList)
    notesRecyclerView.adapter = adapter

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
      noteList.clear()
      noteList.addAll(noteDao.getAll())
      runOnUiThread {
        adapter.notifyDataSetChanged()
      }
    }
  }
}