package com.danilodequeiroz.notes_persistence

import io.reactivex.Flowable
import io.reactivex.Single


interface LoginDataRepository {

    fun getAllLogins(): Flowable<MutableList<LoginData>>
    fun getLogin(email: String): Flowable<LoginData>
    fun insertLogin(loginData: LoginData): Single<Long>

}