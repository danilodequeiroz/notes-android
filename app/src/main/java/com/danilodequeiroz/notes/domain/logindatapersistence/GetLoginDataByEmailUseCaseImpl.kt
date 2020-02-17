package com.danilodequeiroz.notes.domain.logindatapersistence


import com.danilodequeiroz.notes_persistence.LoginData
import com.danilodequeiroz.notes_persistence.LoginDataRepository
import io.reactivex.Flowable

class GetLoginDataByEmailUseCaseImpl(val loginDataRepository: LoginDataRepository) : GetLoginDataByEmailUseCase {
    override fun getLogin(email: String): Flowable<LoginData>{
        return loginDataRepository.getLogin(email)
    }
}