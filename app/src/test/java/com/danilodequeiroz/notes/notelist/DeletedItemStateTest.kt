package com.danilodequeiroz.notes.notelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.danilodequeiroz.notes.domain.notepersistence.DeleteAllNotesUseCase
import com.danilodequeiroz.notes.domain.notepersistence.DeleteNoteByIdUseCase
import com.danilodequeiroz.notes.domain.notepersistence.GetNoteByIdUseCase
import com.danilodequeiroz.notes.domain.notepersistence.GetNoteListUseCase
import com.danilodequeiroz.notes.mocks.NOTE_LIST
import com.danilodequeiroz.notes.ui.notelist.NoteListViewModel
import com.danilodequeiroz.notes.ui.notelist.state.DeletedItemState
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


class DeletedItemStateTest {
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
    fun given_get_all_notes_and_delete_one_then_Success_Delete_States() {
        val givenResponse = NOTE_LIST
        val givenDeletedId = NOTE_LIST.first().noteId!!
        whenever(noteListUseCase.getAllNotes())
            .doReturn(Flowable.just(givenResponse))
        whenever(deleteNoteByIdUseCase.deleteNoteById(givenDeletedId))
            .doReturn(Single.just(Unit))
        viewModel.viewState().observeForever(observerState)

        viewModel.deleteNote(givenDeletedId)

        verify(noteListUseCase).getAllNotes()
        verify(deleteNoteByIdUseCase).deleteNoteById(any())
        val argumentCaptor = argumentCaptor<ViewState>()
        val loading = LoadingState()
        val success = SuccessState(NOTE_LIST)
        val deleted = DeletedItemState(givenDeletedId)
        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(this.capture())

            val ( initialState, deletedState) = allValues

            assertEquals(initialState, success)
            assertEquals(deletedState, deleted)
        }
    }



}
