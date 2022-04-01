package com.example.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import com.example.notes.data.NotesDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
  lateinit var noteDao: NoteDao
  lateinit var adapter: RecyclerView.Adapter<*>
  lateinit var dataset: List<Note>
  lateinit var noteList: MutableList<Note>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    noteDao = NotesDB.getDatabase(this).noteDao()

    val notesRecyclerView = findViewById<RecyclerView>(R.id.notes_recycle_view)
    noteList = mutableListOf()
    adapter = NotesAdapter(noteList)
    notesRecyclerView.adapter = adapter

    /// Filter button
    val filter = findViewById<RadioGroup>(R.id.note_color_filter)
    filter.setOnCheckedChangeListener { _, id ->
      filterDataset(
        when (id) {
          R.id.filter_blue  -> getColor(R.color.note_blue)
          R.id.filter_green -> getColor(R.color.note_green)
          R.id.filter_yellow-> getColor(R.color.note_yellow)
          R.id.filter_red   -> getColor(R.color.note_red)
          else -> null
        }
      )
    }

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
      dataset = noteDao.getAll()
      runOnUiThread {
        /// clear the filter also updates the view
        val filter = findViewById<RadioGroup>(R.id.note_color_filter)
        filter.clearCheck()
      }
    }
  }

  private fun updateView(data: List<Note>) {
      noteList.clear()
      noteList.addAll(data)
      adapter.notifyDataSetChanged()
  }

  private fun filterDataset(color: Int?) {
    if (color === null) {
      updateView(dataset)
    } else {
      val filterData = dataset.filter { it.color == color }
      updateView(filterData)
    }
  }
}