package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioGroup
import androidx.room.Room
import com.example.notes.data.Note
import com.example.notes.data.NotesDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditNote : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_edit_note)

    /// DATABASE INITIALIZATION
    val db = Room.databaseBuilder(
      applicationContext,
      NotesDB::class.java, "notes-database"
    ).build()
    val noteDao = db.noteDao()

    var title = ""
    var color = getColor(R.color.note_blue)
    var content = ""

    val colorSelector = findViewById<RadioGroup>(R.id.note_color_selector)
    val inputTitle = findViewById<EditText>(R.id.input_note_title)
    val inputContent = findViewById<EditText>(R.id.input_note_content)
    val submitBtn = findViewById<FloatingActionButton>(R.id.input_note_confirm_fab)
    submitBtn.setOnClickListener {
      title = inputTitle.text.toString().trim()
      content = inputContent.text.toString().trim()
      color = getColorFromSelector(colorSelector.checkedRadioButtonId)
      val note = Note(title, color, content)

      CoroutineScope(Dispatchers.IO).launch {
        noteDao.insert(note)
        runOnUiThread {
          Log.i("noteid", note.id.toString())
          finish()
        }
      }
    }
  }

  private fun getColorFromSelector(checkedId: Int): Int{
    return when (checkedId) {
      R.id.input_note_green-> getColor(R.color.note_green)
      R.id.input_note_yellow-> getColor(R.color.note_yellow)
      R.id.input_note_red-> getColor(R.color.note_red)
      else -> getColor(R.color.note_blue)
    }
  }

}