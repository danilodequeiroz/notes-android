package com.danilodequeiroz.notes.ui.login.state

import com.danilodequeiroz.notes.domain.firebaseauth.LoginDataResult
import com.danilodequeiroz.notes.domain.firebaseauth.LoginDataState
import com.danilodequeiroz.notes_persistence.LoginData


sealed class ViewState {
    abstract val data: LoginDataResult?
}
//@formatter:off
data class FetchedLoginDataState(val loginData: MutableList<LoginData>, override val data: LoginDataResult? = null) : ViewState()
data class SuccessState(override val data: LoginDataResult) : ViewState()
data class LoadingState(override val data: LoginDataResult? = null) : ViewState()
data class ErrorState(
    val errorMessage: String?,
    override val data: LoginDataResult? = LoginDataResult(
        LoginDataState.ERROR,
        errorMessage
    )
) : ViewState()
//@formatter:on