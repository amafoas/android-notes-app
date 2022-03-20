package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class ExpandedNote : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_expanded_note)

    val title = intent.getStringExtra("title")
    val content = intent.getStringExtra("content")
    val color = intent.getIntExtra("color", 0)

    val container = findViewById<ConstraintLayout>(R.id.expanded_note_container)
    val titleView = findViewById<TextView>(R.id.expanded_note_title)
    val contentView = findViewById<TextView>(R.id.expanded_note_content)
    titleView.text = title
    contentView.text = content
    container.setBackgroundColor(color)
  }
}