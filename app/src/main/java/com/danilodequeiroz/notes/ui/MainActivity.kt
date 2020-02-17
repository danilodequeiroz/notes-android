package com.danilodequeiroz.notes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danilodequeiroz.notes.ui.notelist.NoteListFragment
import kotlinx.android.synthetic.main.main_activity.*
import android.content.Intent
import com.danilodequeiroz.notes.R
import com.danilodequeiroz.notes.ui.noteupdate.CreateUpdateNoteActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    NoteListFragment.newInstance(),
                    NoteListFragment::class.java.simpleName
                )
                .commitNow()
        }
        setupAddFab()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupAddFab() {
        fabAddNote.setOnClickListener {
            val intent = Intent(this, CreateUpdateNoteActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            overridePendingTransition(
                R.anim.bottom_up,
                R.anim.nothing
            )
        }
    }

}
