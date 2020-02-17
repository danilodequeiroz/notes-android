package com.danilodequeiroz.notes.notelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.danilodequeiroz.notes.domain.notepersistence.DeleteAllNotesUseCase
import com.danilodequeiroz.notes.domain.notepersistence.DeleteNoteByIdUseCase
import com.danilodequeiroz.notes.domain.notepersistence.GetNoteByIdUseCase
import com.danilodequeiroz.notes.domain.notepersistence.GetNoteListUseCase
import com.danilodequeiroz.notes.mocks.NOTE_LIST
import com.danilodequeiroz.notes.ui.notelist.NoteListViewModel
import com.danilodequeiroz.notes.ui.notelist.state.LoadingState
import com.danilodequeiroz.notes.ui.notelist.state.SuccessState
import com.danilodequeiroz.notes.ui.notelist.state.UpdatedItemState
import com.danilodequeiroz.notes.ui.notelist.state.ViewState
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers


import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule


class UpdatedItemStateTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    val noteByIdUseCase = mock<GetNoteByIdUseCase>()
    val noteListUseCase = mock<GetNoteListUseCase>()
    val deleteAllNotesUseCase = mock<DeleteAllNotesUseCase>()
    val deleteNoteByIdUseCase = mock<DeleteNoteByIdUseCase>()
    val observerState = mock<Observer<ViewState>>()

    val viewModel by lazy {
        NoteListViewModel(
            noteListUseCase,
            noteByIdUseCase,
            deleteNoteByIdUseCase,
            deleteAllNotesUseCase,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Before
    fun initTest() {
        reset(
            noteByIdUseCase,
            noteListUseCase,
            deleteNoteByIdUseCase,
            deleteAllNotesUseCase,
            observerState
        )
    }


    @Test
    fun given_get_all_notes_updated_or_created_one_then_Success_Empty_States() {
        val givenResponse = NOTE_LIST.toMutableList()
        givenResponse.removeAt(3)
        val givenNote = NOTE_LIST[3]
        val givenNoteId = NOTE_LIST[3].noteId!!
        whenever(noteListUseCase.getAllNotes())
            .doReturn(Flowable.just(givenResponse))
        whenever(noteByIdUseCase.getNoteById(givenNoteId))
            .doReturn(Flowable.just(givenNote))
        viewModel.viewState().observeForever(observerState)

        viewModel.getNote(givenNoteId)

        verify(noteListUseCase).getAllNotes()
        verify(noteByIdUseCase).getNoteById(givenNoteId)
        val argumentCaptor = argumentCaptor<com.danilodequeiroz.notes.ui.notelist.state.ViewState>()

        val success = SuccessState(givenResponse)
        val updated = UpdatedItemState(mutableListOf(NOTE_LIST[3]))
        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(this.capture())

            val ( initialState, updatedState) = allValues
            assertEquals(initialState, success)
            assertEquals(updatedState, updated)

        }
    }


}
