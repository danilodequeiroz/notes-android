package com.danilodequeiroz.notes.domain.logindatapersistence


import io.reactivex.Single

interface InsertLoginDataUseCase {
    fun insertLogion(email: String, password: String): Single<Long>
}