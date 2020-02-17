package com.danilodequeiroz.notes.domain.logindatapersistence


import com.danilodequeiroz.notes_persistence.LoginData
import com.danilodequeiroz.notes_persistence.LoginDataRepository
import io.reactivex.Single

class InsertLoginDataUseCaseImpl(val loginDataRepository: LoginDataRepository) :
    InsertLoginDataUseCase {
    override fun insertLogion(email: String, password: String): Single<Long> {
        return loginDataRepository.insertLogin(LoginData(email, password))
    }
}