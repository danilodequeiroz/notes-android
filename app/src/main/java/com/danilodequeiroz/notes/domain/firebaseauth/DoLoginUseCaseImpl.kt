package com.danilodequeiroz.notes.domain.firebaseauth


import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleOnSubscribe

class DoLoginUseCaseImpl() : DoLoginUseCase {


    override fun login(email: String, password: String): Single<LoginDataResult> {
        return Single.create(object : SingleOnSubscribe<LoginDataResult> {
            override fun subscribe(emitter: SingleEmitter<LoginDataResult>) {

                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(object : OnSuccessListener<AuthResult> {
                        override fun onSuccess(authResult: AuthResult?) {
                            emitter.onSuccess(
                                LoginDataResult(
                                    LoginDataState.SUCCESS
                                )
                            )
                        }
                    })
                    .addOnFailureListener(object : OnFailureListener {
                        override fun onFailure(exception: Exception) {
                            emitter.onError(exception)
                        }
                    })
            }
        })
    }
}

