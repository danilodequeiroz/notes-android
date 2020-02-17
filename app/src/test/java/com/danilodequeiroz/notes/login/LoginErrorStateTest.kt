package com.danilodequeiroz.notes.login

import android.text.TextUtils
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.danilodequeiroz.notes.domain.firebaseauth.DoLoginUseCase
import com.danilodequeiroz.notes.domain.firebaseauth.LoginDataResult
import com.danilodequeiroz.notes.domain.firebaseauth.LoginDataState
import com.danilodequeiroz.notes.domain.logindatapersistence.GetAllLoginDataUseCase
import com.danilodequeiroz.notes.domain.logindatapersistence.InsertLoginDataUseCase
import com.danilodequeiroz.notes.mocks.LOGIN_DATA_LIST
import com.danilodequeiroz.notes.ui.login.LoginViewModel
import com.danilodequeiroz.notes.ui.login.state.*
import com.google.firebase.auth.FirebaseAuthException
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class LoginErrorStateTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    val doLoginUseCase = mock<DoLoginUseCase>()
    val getAllLoginDataUseCase = mock<GetAllLoginDataUseCase>()
    val insertLoginDataUseCase = mock<InsertLoginDataUseCase>()

    val observerState = mock<Observer<ViewState>>()

    val viewModel by lazy {
        LoginViewModel(
            doLoginUseCase,
            getAllLoginDataUseCase,
            insertLoginDataUseCase,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @Before
    fun initTest() {
        reset(
            doLoginUseCase,
            getAllLoginDataUseCase,
            insertLoginDataUseCase,
            observerState
        )
    }


    @Test
    fun given_login_has_erroneous_data_then_Error_States() {
        RxJavaPlugins.setErrorHandler {} // Prevent exception deliver warnings
        val givenResponse = LOGIN_DATA_LIST
        whenever(getAllLoginDataUseCase.getAllLogin())
            .doReturn(Flowable.just(givenResponse))

        whenever(doLoginUseCase.login("any", "any"))
            .doReturn(Single.just(LoginDataResult(LoginDataState.ERROR, "error")))

        viewModel.viewState().observeForever(observerState)
        viewModel.login("any", "any", false)

        verify(getAllLoginDataUseCase).getAllLogin()
        verify(doLoginUseCase).login("any", "any")
        val argumentCaptor = argumentCaptor<ViewState>()
        val successLoginDatas = FetchedLoginDataState(LOGIN_DATA_LIST)
        val givenLoading = LoadingState()
        val errorInLogin = ErrorState("error", LoginDataResult(LoginDataState.ERROR, "error"))
        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(this.capture())

            val (fetchedLoginData, loading, loginError) = allValues
            assertEquals(fetchedLoginData, successLoginDatas)
            assertEquals(givenLoading, loading)
            assertEquals(errorInLogin, loginError)
        }
    }


    @Test
    fun given_login_trhows_error_data_then_Error_States() {
        RxJavaPlugins.setErrorHandler {} // Prevent exception deliver warnings
        val givenResponse = LOGIN_DATA_LIST
        whenever(getAllLoginDataUseCase.getAllLogin())
            .doReturn(Flowable.just(givenResponse))

        whenever(doLoginUseCase.login("any", "any"))
            .doReturn(Single.error(FirebaseAuthException("some firebase error","some firebase error")))

        viewModel.viewState().observeForever(observerState)
        viewModel.login("any", "any", false)

        verify(getAllLoginDataUseCase).getAllLogin()
        verify(doLoginUseCase).login("any", "any")

        val argumentCaptor = argumentCaptor<ViewState>()
        val thenFetched = FetchedLoginDataState(LOGIN_DATA_LIST)
        val thenLoading = LoadingState()
        val thenError = ErrorState("some firebase error")
        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(this.capture())

            val (fetched, loading, success) = allValues
            assertEquals(thenFetched, fetched)
            assertEquals(thenLoading, loading)
            assertEquals(thenError, success)
        }
    }


}
