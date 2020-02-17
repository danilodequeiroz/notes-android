package com.danilodequeiroz.notes.ui.noteupdate

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import com.danilodequeiroz.notes.R
import com.danilodequeiroz.notes.ui.item.NoteItemViewModel
import com.danilodequeiroz.notes.ui.noteupdate.state.ErrorState
import com.danilodequeiroz.notes.ui.noteupdate.state.LoadingState
import com.danilodequeiroz.notes.ui.noteupdate.state.SuccessUpdateState
import com.danilodequeiroz.notes.ui.noteupdate.state.ViewState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.create_update_note_activity.*
import kotlinx.android.synthetic.main.create_update_note_activity.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

const val UPDATE_NOTE = "UPDATE_NOTE"

class CreateUpdateNoteActivity : AppCompatActivity() {

    private val viewModel: CreateUpdateNoteViewModel by viewModel()

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.nothing, R.anim.bottom_down)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_update_note_activity)
        setupToolbar()
        setupSpinner()
        setupViewEvents()
        viewModel.viewState().observe(this, Observer { handleViewState(it) })
        if (isNoteUpdating()) {
            updateFieldsFromNote()
        }
    }

    private fun updateFieldsFromNote() {
        toolbar_title.setText( R.string.update_note)
        noteInUpdate()?.let { note ->
            editTextTitle.setText(note.title)
            editTextDescription.setText(note.title)
            spinnerPriority.setText(note.priority.toString())
        }
    }

    private fun isNoteUpdating() = intent.getParcelableExtra<NoteItemViewModel>(UPDATE_NOTE) != null
    private fun noteInUpdate() = intent.getParcelableExtra<NoteItemViewModel>(UPDATE_NOTE)

    private fun handleViewState(it: ViewState?) {
        when (it) {
            is LoadingState -> showFeedbackSnackBar(R.string.loading)
            is SuccessUpdateState -> finish()
            is ErrorState -> showFeedbackSnackBar(R.string.technical_error_message)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupSpinner() {
        spinnerPriority.setOnClickListener {
            PopupMenu(this, spinnerPriority).apply {
                menuInflater.inflate(R.menu.menu_priority, menu)
                setOnMenuItemClickListener { priorityItem ->
                    val title = priorityItem.title.toString()
                    spinnerPriority.setText(title)
                    spinnerPriority.contentDescription =
                        getString(R.string.cd_selected_priority, title)
                    true
                }
                show()
            }
        }
    }

    private fun setupViewEvents() {
        fabUpdate.setOnClickListener {
            if (!fieldsValid())
                return@setOnClickListener
            val noteId = if(isNoteUpdating()) noteInUpdate()?.noteId else null

            viewModel.updateNote(
                noteId,
                editTextTitle.text.toString(),
                editTextDescription.text.toString(),
                spinnerPriority.text?.toString()?.toIntOrNull() ?: 1
            )
        }
    }

    private fun fieldsValid(): Boolean {
        val titleValid = if (editTextTitle.text.isNullOrEmpty()) {
            textInputTitle.error = getString(R.string.field_required)
            false
        } else {
            textInputTitle.error = null
            true
        }
        val descriptionValid = if (editTextDescription.text.isNullOrEmpty()) {
            textInputDescription.error = getString(R.string.field_required)
            false
        } else {
            textInputDescription.error = null
            true
        }
        val priorityValid = if (spinnerPriority.text.isNullOrEmpty()) {
            textInputPriority.error = getString(R.string.field_required)
            false
        } else {
            textInputPriority.error = null
            true
        }
        return (titleValid && descriptionValid && priorityValid)
    }

    private fun showFeedbackSnackBar(@StringRes stringRes: Int) {
        val snackBar = Snackbar.make(
            coordinatorLayout,
            stringRes,
            Snackbar.LENGTH_LONG
        )
        snackBar.show()
    }
}
