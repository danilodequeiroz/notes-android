package com.danilodequeiroz.notes.domain.logindatapersistence


import com.danilodequeiroz.notes_persistence.LoginData
import com.danilodequeiroz.notes_persistence.LoginDataRepository
import io.reactivex.Flowable

class GetAllLoginDataUseCaseImpl(val repository: LoginDataRepository) :
    GetAllLoginDataUseCase {
    override fun getAllLogin(): Flowable<MutableList<LoginData>> {
        return repository.getAllLogins()
    }
}