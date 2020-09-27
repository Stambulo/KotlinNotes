package com.stambulo.kotlinnotes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.view.*

import com.stambulo.kotlinnotes.R
import com.stambulo.kotlinnotes.data.entity.Note
import com.stambulo.kotlinnotes.extensions.getColorInt

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

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(note: Note) = with(itemView as CardView) {
            tv_title.text = note.title
            tv_text.text = note.text

            setCardBackgroundColor(note.color.getColorInt(context))

            itemView.setOnClickListener{
                onItemClick?.invoke(note)
            }
        }
    }


}