package com.example.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioGroup
import androidx.room.Room
import com.example.notes.data.Note
import com.example.notes.data.NoteDao
import com.example.notes.data.NotesDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditNote : AppCompatActivity() {
  lateinit var db: NotesDB
  lateinit var noteDao: NoteDao

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_edit_note)

    db = Room.databaseBuilder(
      applicationContext,
      NotesDB::class.java, "notes-database"
    ).build()
    noteDao = db.noteDao()

    val inputTitle = findViewById<EditText>(R.id.input_note_title)
    val inputContent = findViewById<EditText>(R.id.input_note_content)
    val submitBtn = findViewById<FloatingActionButton>(R.id.input_note_confirm_fab)

    val isEdition = intent.getBooleanExtra("isEdition", false)
    if (isEdition){
      inputTitle.setText(intent.getStringExtra("title"))
      inputContent.setText(intent.getStringExtra("content"))
      setColorSelectorTo(intent.getIntExtra("color", 0))
    }

    submitBtn.setOnClickListener {
      val title = inputTitle.text.toString().trim()
      val content = inputContent.text.toString().trim()
      val color = getColorFromSelector()
      val note = Note(title, color, content)
      if (isEdition) note.id = intent.getIntExtra("id", note.id)
      sendToDatabase(note, isEdition)
    }
  }

  private fun sendToDatabase(note: Note, isEdition: Boolean){
    CoroutineScope(Dispatchers.IO).launch {
      if (isEdition){
        noteDao.update(note)
        // response for ExpandedNote
        val intent = Intent()
        intent.putExtra("newTitle", note.title)
        intent.putExtra("newColor", note.color)
        intent.putExtra("newContent", note.content)
        setResult(RESULT_OK, intent)
      } else noteDao.insert(note)
      runOnUiThread {
        finish()
      }
    }
  }

  private fun setColorSelectorTo(color: Int) {
    val colorSelector = findViewById<RadioGroup>(R.id.note_color_selector)
    val selector = when (color) {
      getColor(R.color.note_green) -> R.id.input_note_green
      getColor(R.color.note_yellow) -> R.id.input_note_yellow
      getColor(R.color.note_red) -> R.id.input_note_red
      else -> R.id.input_note_blue
    }
    colorSelector.check(selector)
  }

  private fun getColorFromSelector(): Int{
    val colorSelector = findViewById<RadioGroup>(R.id.note_color_selector)
    return when (colorSelector.checkedRadioButtonId) {
      R.id.input_note_green-> getColor(R.color.note_green)
      R.id.input_note_yellow-> getColor(R.color.note_yellow)
      R.id.input_note_red-> getColor(R.color.note_red)
      else -> getColor(R.color.note_blue)
    }
  }
}