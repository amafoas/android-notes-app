package com.example.notes

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.data.Note

class NotesAdapter (private val noteList: List<Note>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
  // References to the view
  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.note_title)
    val content: TextView = itemView.findViewById(R.id.note_content)
    val card: CardView = itemView.findViewById(R.id.note_CardView)
  }

  // Create new views (invoked by the layout manager)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    // Create a new view, which defines the UI of the list item
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.note_item, parent, false)

    return ViewHolder(view)
  }

  // Replace the contents of a view (invoked by the layout manager)
  override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
    val currNote : Note = noteList[position]
    // Get element from your dataset at this position and replace the
    // contents of the view with that element
    viewHolder.title.text = currNote.title
    viewHolder.content.text = currNote.content
    viewHolder.card.setCardBackgroundColor(currNote.color)

    viewHolder.card.setOnClickListener {
      if (it != null){
        val context = it.context
        val intent = Intent(context, ExpandedNote::class.java)
        intent.putExtra("title", currNote.title)
        intent.putExtra("color", currNote.color)
        intent.putExtra("content", currNote.content)
        intent.putExtra("id", currNote.id)
        context.startActivity(intent)
      }
    }
  }

  // Return the size of your dataset (invoked by the layout manager)
  override fun getItemCount(): Int = noteList.size
}