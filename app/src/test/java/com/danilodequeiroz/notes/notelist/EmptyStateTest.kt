package com.danilodequeiroz.notes.notelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.danilodequeiroz.notes.domain.notepersistence.DeleteAllNotesUseCase
import com.danilodequeiroz.notes.domain.notepersistence.DeleteNoteByIdUseCase
import com.danilodequeiroz.notes.domain.notepersistence.GetNoteByIdUseCase
import com.danilodequeiroz.notes.domain.notepersistence.GetNoteListUseCase
import com.danilodequeiroz.notes.mocks.NOTE_LIST
import com.danilodequeiroz.notes.ui.item.NoteItemViewModel
import com.danilodequeiroz.notes.ui.notelist.NoteListViewModel
import com.danilodequeiroz.notes.ui.notelist.state.*
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class EmptyStateTest {
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
    fun given_get_no_notes_at_all_then_Empty_States() {
        val givenResponse = mutableListOf<NoteItemViewModel>()
        whenever(noteListUseCase.getAllNotes())
            .doReturn(Flowable.just(givenResponse))

        viewModel.viewState().observeForever(observerState)

        verify(noteListUseCase).getAllNotes()
        val argumentCaptor = argumentCaptor<ViewState>()
        val success = EmptyState()
        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(this.capture())

            val (initialState) = allValues
            assertEquals(initialState, success)
        }
    }


}
