package com.danilodequeiroz.notes.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.danilodequeiroz.notes.domain.firebaseauth.DoLoginUseCase
import com.danilodequeiroz.notes.domain.firebaseauth.LoginDataResult
import com.danilodequeiroz.notes.domain.firebaseauth.LoginDataState
import com.danilodequeiroz.notes.domain.logindatapersistence.GetAllLoginDataUseCase
import com.danilodequeiroz.notes.domain.logindatapersistence.InsertLoginDataUseCase
import com.danilodequeiroz.notes.mocks.LOGIN_DATA_LIST
import com.danilodequeiroz.notes.ui.login.LoginViewModel
import com.danilodequeiroz.notes.ui.login.state.FetchedLoginDataState
import com.danilodequeiroz.notes.ui.login.state.LoadingState
import com.danilodequeiroz.notes.ui.login.state.SuccessState
import com.danilodequeiroz.notes.ui.login.state.ViewState
import com.danilodequeiroz.notes_persistence.LoginData
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class LoginSuccessStateTest {
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
    fun given_login_data_fetches_when_login_without_save_option_then_Success_States() {
        val givenResponse = LOGIN_DATA_LIST
        whenever(getAllLoginDataUseCase.getAllLogin())
            .doReturn(Flowable.just(givenResponse))

        whenever(doLoginUseCase.login("any","any"))
            .doReturn(Single.just(LoginDataResult(LoginDataState.SUCCESS)))

        viewModel.viewState().observeForever(observerState)
        viewModel.login("any","any",false)

        verify(getAllLoginDataUseCase).getAllLogin()
        verify(doLoginUseCase).login("any","any")
        val argumentCaptor = argumentCaptor<ViewState>()
        val successLoginDatas = FetchedLoginDataState(LOGIN_DATA_LIST)
        val givenLoading = LoadingState()
        val successLoginAction = SuccessState(LoginDataResult(LoginDataState.SUCCESS))
        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(this.capture())

            val (fetchedLoginData,loading,successLogin) = allValues
            assertEquals(fetchedLoginData, successLoginDatas)
            assertEquals(givenLoading, loading)
            assertEquals(successLoginAction, successLogin)
        }
    }


    @Test
    fun given_login_data_fetches_when_login_with_saving_option_then_Success_States() {
        val givenResponse = LOGIN_DATA_LIST
        whenever(getAllLoginDataUseCase.getAllLogin())
            .doReturn(Flowable.just(givenResponse))

        whenever(doLoginUseCase.login("any","any"))
            .doReturn(Single.just(LoginDataResult(LoginDataState.SUCCESS)))

        whenever(insertLoginDataUseCase.insertLogion("any","any"))
            .doReturn(Single.just(1L))

        viewModel.viewState().observeForever(observerState)
        viewModel.login("any","any",true)

        verify(getAllLoginDataUseCase).getAllLogin()
        verify(doLoginUseCase).login("any","any")
        verify(insertLoginDataUseCase).insertLogion("any","any")
        val argumentCaptor = argumentCaptor<ViewState>()
        val successLoginDatas = FetchedLoginDataState(LOGIN_DATA_LIST)
        val givenLoading = LoadingState()
        val successLoginAction = SuccessState(LoginDataResult(LoginDataState.SUCCESS))
        argumentCaptor.run {
            verify(observerState, times(3)).onChanged(this.capture())

            val (fetchedLoginData,loading,successLogin) = allValues
            assertEquals(fetchedLoginData, successLoginDatas)
            assertEquals(givenLoading, loading)
            assertEquals(successLoginAction, successLogin)
        }
    }


}
