package com.example.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.notes.data.Note
import com.example.notes.data.NotesDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExpandedNote : AppCompatActivity() {
  var title : String = ""
  var content : String = ""
  var color : Int = 0
  var id : Int = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_expanded_note)

    title = intent.getStringExtra("title").toString()
    content = intent.getStringExtra("content").toString()
    color = intent.getIntExtra("color", 0)
    id = intent.getIntExtra("id", 0)
  }

  override fun onResume() {
    super.onResume()

    val container = findViewById<ConstraintLayout>(R.id.expanded_note_container)
    val titleView = findViewById<TextView>(R.id.expanded_note_title)
    val contentView = findViewById<TextView>(R.id.expanded_note_content)

    titleView.text = title
    contentView.text = content
    container.setBackgroundColor(color)
  }

  private val resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
    if (result.resultCode == RESULT_OK) {
      val data: Intent? = result.data
      if (data != null){
        title = data.getStringExtra("newTitle").toString()
        color = data.getIntExtra("newColor", 0)
        content = data.getStringExtra("newContent").toString()
      }
    }
  }

  // called in xml by edit_note_fab
  fun goToEditNote(view: View){
    val intent = Intent(this, EditNote::class.java)
    intent.putExtra("isEdition", true)
    intent.putExtra("title", title)
    intent.putExtra("color", color)
    intent.putExtra("content", content)
    intent.putExtra("id", id)

    resultLauncher.launch(intent)
  }

  // called in xml
  fun showDeleteModal(view: View){
    val deleteModal = findViewById<LinearLayout>(R.id.delete_modal)
    deleteModal.visibility = View.VISIBLE
  }

  // called in xml
  fun hideDeleteModal(view: View){
    val deleteModal = findViewById<LinearLayout>(R.id.delete_modal)
    deleteModal.visibility = View.GONE
  }

  // called in xml by delete_note_fab
  fun confirmDeletion(view: View){
    val noteDao = NotesDB.getDatabase(this).noteDao()

    val note = Note("",0,"")
    note.id = id
    CoroutineScope(Dispatchers.IO).launch {
      noteDao.delete(note)
      runOnUiThread {
        finish()
      }
    }
  }

  // called in xml by expanded_note_menu_fab
  fun expandMenu(view: View){
    val menuFab = (view as FloatingActionButton)
    val editNoteFAB   = findViewById<FloatingActionButton>(R.id.edit_note_fab)
    val deleteNoteFAB = findViewById<FloatingActionButton>(R.id.delete_note_fab)
    val expanded = editNoteFAB.isVisible

    if (expanded) {
      menuFab.setImageResource(R.drawable.ic_baseline_expand_less_24)
      editNoteFAB.visibility = View.INVISIBLE
      deleteNoteFAB.visibility = View.INVISIBLE
    } else {
      menuFab.setImageResource(R.drawable.ic_baseline_expand_more_24)
      editNoteFAB.visibility = View.VISIBLE
      deleteNoteFAB.visibility = View.VISIBLE
    }
  }
}