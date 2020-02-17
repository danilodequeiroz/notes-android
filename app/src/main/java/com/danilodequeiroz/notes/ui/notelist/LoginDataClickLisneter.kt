package com.danilodequeiroz.notes.ui.notelist

import com.danilodequeiroz.notes.ui.item.NoteItemViewModel
import com.danilodequeiroz.notes_persistence.LoginData

interface LoginDataClickLisneter{
    fun onLoginDataCicked(item :LoginData)
}
