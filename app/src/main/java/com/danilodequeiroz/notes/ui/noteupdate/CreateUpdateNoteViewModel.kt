package com.danilodequeiroz.notes.ui.noteupdate

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danilodequeiroz.notes.R
import com.danilodequeiroz.notes.domain.notepersistence.UpdateNoteUseCase
import com.danilodequeiroz.notes.ui.noteupdate.state.*
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class CreateUpdateNoteViewModel(
    val updateNoteUseCase: UpdateNoteUseCase,
    val subscribeOnScheduler: Scheduler,
    val observeOnScheduler: Scheduler
) : ViewModel() {

    val compositeDisposable = CompositeDisposable()
    val viewState = MutableLiveData<ViewState>()

    fun viewState() : LiveData<ViewState> = viewState

    fun updateNote(noteId: Long? = null, title: String, description: String, priority: Int) {
        viewState.postValue(LoadingState())
        compositeDisposable.add(
            updateNoteUseCase.updateNote(noteId,
                title,
                description,
                priority)
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(
                    { updatedNoteId -> viewState.postValue(SuccessUpdateState(updatedNoteId)) },
                    { error -> errorState(error) }
                )
        )
    }

    private fun errorState(error: Throwable) {
        viewState.postValue(ErrorState(R.string.technical_error_message))
        Log.e(CreateUpdateNoteViewModel::class.java.simpleName, error.message, error)
    }

}
