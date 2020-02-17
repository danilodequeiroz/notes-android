package com.danilodequeiroz.notes.ui.widget

import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView

import com.danilodequeiroz.notes.R
import com.danilodequeiroz.notes.ui.item.NoteItemViewModel
import com.danilodequeiroz.notes.ui.notelist.NoteClickLisneter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.note_list_item.*

class NotesRecyclerViewAdapter(val itemClickListener: NoteClickLisneter) :
    RecyclerView.Adapter<NotesRecyclerViewAdapter.MyViewHolder>() {

    val notes: MutableList<NoteItemViewModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = notes[position].title
        holder.description.text = notes[position].description
        holder.priority.text = holder.containerView.context?.getString(R.string.priority_format,notes[position].priority.toString())
        holder.card.setOnClickListener { itemClickListener.onNoteCicked(notes[position]) }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun populateNotes(items: MutableList<NoteItemViewModel>) {
        notes.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        notes.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeAllItems() {
        notes.clear()
        notifyDataSetChanged()
    }

    fun restoreItem(item: NoteItemViewModel, position: Int) {
        notes.add(position, item)
        notifyItemInserted(position)
    }

    class MyViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        val title: TextView = textViewTitle
        val description: TextView = textViewDescription
        val priority: TextView = textViewPriority
        val card: CardView = cardView
    }
}

