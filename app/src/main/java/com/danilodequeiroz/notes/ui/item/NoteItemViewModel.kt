package com.danilodequeiroz.notes.ui.item

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class NoteItemViewModel(

    val noteId: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val priority: Int? = null,
    val priorityRange : MutableList<Int> = mutableListOf(1,2,3,4,5)

) : Parcelable