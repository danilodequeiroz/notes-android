package com.danilodequeiroz.notes_persistence


import io.reactivex.Flowable
import io.reactivex.Single


class LoginDataRepositoryImpl(val loginDataDao: LoginDataDao) : LoginDataRepository {

    override fun getAllLogins(): Flowable<MutableList<LoginData>> {
        return loginDataDao.getAllLogins()
    }

    override fun getLogin(email:String): Flowable<LoginData> {
        return loginDataDao.getLogin(email)
    }

    override fun insertLogin(loginData: LoginData): Single<Long> {
        return loginDataDao.insertLogin(loginData)
    }


}