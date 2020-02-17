package com.danilodequeiroz.notes.domain.firebaseauth

data class LoginDataResult(
    val loginDataState: LoginDataState,
    val errorMessage: String? = null
)

enum class LoginDataState {
    SUCCESS, ERROR,
}