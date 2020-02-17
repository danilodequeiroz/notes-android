package com.danilodequeiroz.notes.ui.notelist


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.danilodequeiroz.notes.R
import com.danilodequeiroz.notes.ui.item.NoteItemViewModel
import com.danilodequeiroz.notes.ui.notelist.state.*
import com.danilodequeiroz.notes.ui.widget.NotesRecyclerViewAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.RecyclerView
import com.danilodequeiroz.notes.ui.noteupdate.CreateUpdateNoteActivity
import com.danilodequeiroz.notes.ui.noteupdate.UPDATE_NOTE
import com.danilodequeiroz.notes.ui.widget.MarginItemDecoration
import com.danilodequeiroz.notes.ui.widget.SwipeToDeleteCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.note_list_fragment.*


class NoteListFragment : Fragment(), NoteClickLisneter {

    companion object {
        fun newInstance() = NoteListFragment()
    }

    private val viewModel: NoteListViewModel by viewModel()
    private var adapter: NotesRecyclerViewAdapter? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.viewState().observe(viewLifecycleOwner, Observer { handleViewState(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.remove_menu, menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.note_list_fragment, container, false)
    }

    override fun onNoteCicked(item: NoteItemViewModel) {
        val intent = Intent(context, CreateUpdateNoteActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra(UPDATE_NOTE, item)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_remove_all -> {
                showRemoveAllDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun handleViewState(it: ViewState?) {
        when (it) {
            is LoadingState -> {
            }
            is EmptyState -> {
                showEmptyView()
            }
            is SuccessState -> {
                hideEmptyView()
                populateNotesRecyclerView(it.data)
            }
            is UpdatedItemState -> {
                showFeedbackSnackBar(R.string.note_updated)
            }
            is DeletedItemState -> {
                showFeedbackSnackBar(R.string.note_deleted)
            }
            is ErrorState -> {
                showFeedbackSnackBar(R.string.technical_error_message)
            }
        }
    }

    private fun enableSwipeToDelete() {
        context?.let { ctx ->
            val swipeToDeleteCallback = object : SwipeToDeleteCallback(ctx) {
                val view = activity?.findViewById<View>(android.R.id.content)

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    val position = viewHolder.adapterPosition
                    adapter?.notes?.get(position)?.let { item ->
                        val idToRemove = item.noteId
                        adapter?.removeItem(position)
                        view?.let { view ->
                            val snackBar = Snackbar.make(
                                view,
                                R.string.note_undo_title,
                                Snackbar.LENGTH_LONG
                            )
                            snackBar.setAction(R.string.note_undo_action) {
                                adapter?.restoreItem(item, position)
                                recyclerViewNotes.scrollToPosition(position)
                            }
                            snackBar.addCallback(object : Snackbar.Callback() {
                                override fun onDismissed(snackbar: Snackbar, event: Int) {
                                    idToRemove?.let { it -> delteNote(it) }
                                }
                            })
                            snackBar.setActionTextColor(
                                ContextCompat.getColor(
                                    ctx,
                                    R.color.secondaryDarkColor
                                )
                            )
                            snackBar.show()
                        }
                    }
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(recyclerViewNotes)
        }
    }

    fun delteNote(id: Long) {
        viewModel.deleteNote(id)
    }

    private fun hideEmptyView() {
        if (recyclerViewNotes.visibility == View.GONE && textViewEmpty.visibility == View.VISIBLE) {
            recyclerViewNotes.visibility = View.VISIBLE
            textViewEmpty.visibility = View.GONE
        }
    }

    private fun populateNotesRecyclerView(notes: MutableList<NoteItemViewModel>) {
        adapter = NotesRecyclerViewAdapter(this)
        recyclerViewNotes.adapter = adapter
        recyclerViewNotes.layoutManager = LinearLayoutManager(context)
        recyclerViewNotes.addItemDecoration(
            MarginItemDecoration(
            resources.getDimension(R.dimen.default_padding).toInt())
        )
        adapter?.populateNotes(notes)
        enableSwipeToDelete()
    }

    private fun removeAllNotes() =
        DialogInterface.OnClickListener { dialog, which -> adapter?.removeAllItems(); viewModel.deleteAllNotes() }

    private fun showEmptyView() {
        if (recyclerViewNotes.visibility == View.VISIBLE && textViewEmpty.visibility == View.GONE) {
            recyclerViewNotes.visibility = View.GONE
            textViewEmpty.visibility = View.VISIBLE
        }
    }

    private fun showRemoveAllDialog() {
        MaterialAlertDialogBuilder(context)
            .setMessage(R.string.all_notes_remove_confirmation)
            .setPositiveButton(R.string.ok, removeAllNotes())
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showFeedbackSnackBar(@StringRes stringRes: Int) {
        activity?.findViewById<View>(android.R.id.content)?.let { view ->
            val snackBar = Snackbar.make(
                view,
                stringRes,
                Snackbar.LENGTH_LONG
            )
            snackBar.show()
        }
    }

}
