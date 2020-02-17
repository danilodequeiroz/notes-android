package com.danilodequeiroz.notes.notelist


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.danilodequeiroz.notes.domain.notepersistence.DeleteAllNotesUseCase
import com.danilodequeiroz.notes.domain.notepersistence.DeleteNoteByIdUseCase
import com.danilodequeiroz.notes.domain.notepersistence.GetNoteByIdUseCase
import com.danilodequeiroz.notes.domain.notepersistence.GetNoteListUseCase
import com.danilodequeiroz.notes.mocks.NOTE_LIST
import com.danilodequeiroz.notes.ui.notelist.NoteListViewModel
import com.danilodequeiroz.notes.ui.notelist.state.EmptyState
import com.danilodequeiroz.notes.ui.notelist.state.LoadingState
import com.danilodequeiroz.notes.ui.notelist.state.SuccessState
import com.danilodequeiroz.notes.ui.notelist.state.ViewState
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SuccessStateTest {
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
        reset(noteByIdUseCase,
            noteListUseCase,
            deleteNoteByIdUseCase,
            deleteAllNotesUseCase,
            observerState)
    }

    @Test
    fun given_get_all_notes_Success_States() {
        val givenResponse = NOTE_LIST
        whenever(noteListUseCase.getAllNotes())
            .doReturn(Flowable.just(givenResponse))
        whenever(deleteAllNotesUseCase.deleteAllNotes())
            .doReturn(Single.just(Unit))

        viewModel.viewState().observeForever(observerState)
        viewModel.deleteAllNotes()

        verify(noteListUseCase).getAllNotes()
        verify(deleteAllNotesUseCase).deleteAllNotes()
        val argumentCaptor = argumentCaptor<ViewState>()
        val loading = LoadingState()
        val success = SuccessState(NOTE_LIST)
        val empty = EmptyState()
        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(this.capture())

            val (initialState,emptyState ) = allValues
            assertEquals(initialState, success)
            assertEquals(emptyState, empty)

        }
    }
}
