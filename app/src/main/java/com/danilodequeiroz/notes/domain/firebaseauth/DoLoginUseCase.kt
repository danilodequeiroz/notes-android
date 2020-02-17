package com.danilodequeiroz.notes.domain.firebaseauth


import io.reactivex.Single

interface DoLoginUseCase {
    fun login(email:String, password:String): Single<LoginDataResult>
}