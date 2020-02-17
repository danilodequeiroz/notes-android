package com.danilodequeiroz.notes.ui.notelist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danilodequeiroz.notes.R
import com.danilodequeiroz.notes.domain.notepersistence.DeleteAllNotesUseCase
import com.danilodequeiroz.notes.domain.notepersistence.DeleteNoteByIdUseCase
import com.danilodequeiroz.notes.domain.notepersistence.GetNoteByIdUseCase
import com.danilodequeiroz.notes.domain.notepersistence.GetNoteListUseCase
import com.danilodequeiroz.notes.ui.item.NoteItemViewModel
import com.danilodequeiroz.notes.ui.notelist.state.*
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable


class NoteListViewModel(
    val noteListUseCase: GetNoteListUseCase,
    val noteByIdUseCase: GetNoteByIdUseCase,
    val deleteNoteByIdUseCase: DeleteNoteByIdUseCase,
    val deleteAllNotesUseCase: DeleteAllNotesUseCase,
    val subscribeOnScheduler: Scheduler,
    val observeOnScheduler: Scheduler
) : ViewModel() {

    val compositeDisposable = CompositeDisposable()

    private val viewState = MutableLiveData<ViewState>()


    init {
        getAllNotes()
    }

    fun viewState(): LiveData<ViewState> = viewState

    fun getAllNotes() {
        viewState.postValue(LoadingState())
        compositeDisposable.add(
            noteListUseCase.getAllNotes()
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(
                    { dataList -> postViewState(dataList) },
                    { error -> errorState(error) }
                )
        )
    }

    fun getNote(noteId: Long) {
        compositeDisposable.add(
            noteByIdUseCase.getNoteById(noteId)
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(
                    { data -> viewState.postValue(UpdatedItemState(mutableListOf(data))) },
                    { error -> errorState(error) }
                )
        )
    }

    fun deleteNote(noteId: Long) {
        compositeDisposable.add(
            deleteNoteByIdUseCase.deleteNoteById(noteId)
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(
                    { data -> viewState.postValue(DeletedItemState(noteId)) },
                    { error -> errorState(error) }
                )
        )
    }


    fun deleteAllNotes() {
        compositeDisposable.add(
            deleteAllNotesUseCase.deleteAllNotes()
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(
                    { data -> viewState.postValue(EmptyState()) },
                    { error -> errorState(error) }
                )
        )
    }


    private fun postViewState(dataList: MutableList<NoteItemViewModel>?) {
        viewState.postValue(
            if (!dataList.isNullOrEmpty()) {
                SuccessState(dataList)
            } else {
                EmptyState()
            }
        )
    }

    private fun errorState(error: Throwable) {
        viewState.postValue(ErrorState(R.string.technical_error_message))
        Log.e(NoteListViewModel::class.java.simpleName, error.message, error)
    }

}
