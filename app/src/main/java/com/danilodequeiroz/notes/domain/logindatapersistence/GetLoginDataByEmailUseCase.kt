package com.danilodequeiroz.notes.domain.logindatapersistence


import com.danilodequeiroz.notes_persistence.LoginData
import io.reactivex.Flowable

interface GetLoginDataByEmailUseCase {
    fun getLogin(email: String): Flowable<LoginData>
}