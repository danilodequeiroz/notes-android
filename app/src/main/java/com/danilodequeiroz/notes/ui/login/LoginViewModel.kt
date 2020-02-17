package com.danilodequeiroz.notes.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danilodequeiroz.notes.domain.firebaseauth.DoLoginUseCase
import com.danilodequeiroz.notes.domain.firebaseauth.LoginDataResult
import com.danilodequeiroz.notes.domain.firebaseauth.LoginDataState
import com.danilodequeiroz.notes.domain.logindatapersistence.GetAllLoginDataUseCase
import com.danilodequeiroz.notes.domain.logindatapersistence.InsertLoginDataUseCase
import com.danilodequeiroz.notes.ui.login.state.*
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import java.lang.Exception


class LoginViewModel(
    val doLoginUseCase: DoLoginUseCase,
    val getAllLoginDataUseCase: GetAllLoginDataUseCase,
    val insertLoginDataUseCase: InsertLoginDataUseCase,
    val subscribeOnScheduler: Scheduler,
    val observeOnScheduler: Scheduler
) : ViewModel() {

    private val viewState = MutableLiveData<ViewState>()

    val compositeDisposable = CompositeDisposable()

    fun viewState(): LiveData<ViewState> = viewState

    init {
        getLoginData()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun getLoginData() {
        compositeDisposable.add(
            getAllLoginDataUseCase.getAllLogin()
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(
                    { loginDataResult -> viewState.postValue(FetchedLoginDataState(loginDataResult)) },
                    { error -> errorState(error) }
                )
        )
    }

    private fun insertLoginData(
        email: String,
        password: String,
        loginDataResult: LoginDataResult
    ) {
        compositeDisposable.add(
            insertLoginDataUseCase.insertLogion(email, password)
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(
                    { viewState.postValue(SuccessState(loginDataResult)); },
                    { error -> errorState(error) }
                )
        )
    }

    fun login(email: String, password: String, keepData: Boolean) {
        viewState.postValue(LoadingState())
        compositeDisposable.add(
            doLoginUseCase.login(email, password)
                .subscribeOn(subscribeOnScheduler)
                .observeOn(observeOnScheduler)
                .subscribe(
                    { loginDataResult ->
                        if (loginDataResult.loginDataState == LoginDataState.SUCCESS) {
                            if (keepData) {
                                insertLoginData(email.trim(), password.trim(), loginDataResult)
                            } else {
                                viewState.postValue(SuccessState(loginDataResult));
                            }
                        } else {
                            errorState(Exception(loginDataResult.errorMessage))
                        }
                    },
                    { error -> errorState(error) }
                )
        )
    }

    private fun errorState(error: Throwable) {
        viewState.postValue(ErrorState(error.message))
        Log.e(LoginViewModel::class.java.simpleName, error.message, error)
    }

}
