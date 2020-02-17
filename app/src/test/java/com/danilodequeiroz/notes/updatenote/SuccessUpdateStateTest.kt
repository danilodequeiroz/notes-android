package com.danilodequeiroz.notes.updatenote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.danilodequeiroz.notes.domain.notepersistence.UpdateNoteUseCase
import com.danilodequeiroz.notes.mocks.NOTE_LIST
import com.danilodequeiroz.notes.ui.notelist.state.SuccessState
import com.danilodequeiroz.notes.ui.notelist.state.UpdatedItemState
import com.danilodequeiroz.notes.ui.noteupdate.CreateUpdateNoteViewModel
import com.danilodequeiroz.notes.ui.noteupdate.state.LoadingState
import com.danilodequeiroz.notes.ui.noteupdate.state.SuccessUpdateState
import com.danilodequeiroz.notes.ui.noteupdate.state.ViewState
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule



class SuccessUpdateStateTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    val updateNoteUseCase = mock<UpdateNoteUseCase>()
    val observerState = mock<Observer<ViewState>>()
    val viewModel by lazy {
        CreateUpdateNoteViewModel(
            updateNoteUseCase ,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Before
    fun initTest() {
        reset(updateNoteUseCase,
            observerState)
    }

    @Test
    fun given_get_all_notes_Success_States() {
        val givenNoteId = NOTE_LIST.first().noteId!!
        whenever(updateNoteUseCase.updateNote(givenNoteId,"new title", "new description", 4))
            .doReturn(Single.just(givenNoteId))

        viewModel.viewState.observeForever(observerState)
        viewModel.updateNote(givenNoteId,"new title", "new description", 4)

        verify(updateNoteUseCase).updateNote(givenNoteId,"new title", "new description", 4)
        val argumentCaptor = argumentCaptor<ViewState>()
        val loading = LoadingState()
        val success = SuccessUpdateState(givenNoteId)
        argumentCaptor.run {
            verify(observerState, times(2)).onChanged(this.capture())

            val (loadingState,initialState ) = allValues
            assertEquals(loadingState, loading)
            assertEquals(initialState, success)

        }
    }


}
