package com.danilodequeiroz.notes.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.danilodequeiroz.notes.domain.firebaseauth.DoLoginUseCase
import com.danilodequeiroz.notes.domain.logindatapersistence.GetAllLoginDataUseCase
import com.danilodequeiroz.notes.domain.logindatapersistence.InsertLoginDataUseCase
import com.danilodequeiroz.notes.mocks.LOGIN_DATA_LIST
import com.danilodequeiroz.notes.ui.login.LoginViewModel
import com.danilodequeiroz.notes.ui.login.state.FetchedLoginDataState
import com.danilodequeiroz.notes.ui.login.state.ViewState
import com.danilodequeiroz.notes_persistence.LoginData
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class FetchedLoginDataStateTest {
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
    fun given_get_no_login_data_at_all_then_FetchedLoginData_States() {
        val givenResponse = LOGIN_DATA_LIST
        whenever(getAllLoginDataUseCase.getAllLogin())
            .doReturn(Flowable.just(givenResponse))

        viewModel.viewState().observeForever(observerState)

        verify(getAllLoginDataUseCase).getAllLogin()
        val argumentCaptor = argumentCaptor<ViewState>()
        val success = FetchedLoginDataState(LOGIN_DATA_LIST)
        argumentCaptor.run {
            verify(observerState, times(1)).onChanged(this.capture())

            val (initialState) = allValues
            assertEquals(initialState, success)
        }
    }


}
