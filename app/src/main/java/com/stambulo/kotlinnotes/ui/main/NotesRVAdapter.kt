package com.stambulo.kotlinnotes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.stambulo.kotlinnotes.R
import com.stambulo.kotlinnotes.data.entity.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NotesRVAdapter(val onItemClick: ((Note) -> Unit)? = null) : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = with(itemView as CardView) {
            tv_title.text = note.title
            tv_text.text = note.text

            val color = when(note.color){
                Note.Color.WHITE -> R.color.white
                Note.Color.VIOLET -> R.color.violet
                Note.Color.YELLOW -> R.color.yellow
                Note.Color.RED -> R.color.red
                Note.Color.PINK -> R.color.pink
                Note.Color.GREEN -> R.color.green
            }

            setCardBackgroundColor(ContextCompat.getColor(itemView.context, color))

            itemView.setOnClickListener{
                onItemClick?.invoke(note)
            }
        }
    }


}