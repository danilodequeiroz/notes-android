package com.danilodequeiroz.notes

import android.app.Application
import com.danilodequeiroz.notes.di.databaseModule
import com.danilodequeiroz.notes.di.useCaseModules
import com.danilodequeiroz.notes.di.viewModelModule
import com.facebook.stetho.Stetho
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin


class CustomApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        if(GlobalContext.getOrNull() == null) {
            startKoin {
                // use AndroidLogger as Koin Logger - default Level.INFO
                androidLogger()
                // use the Android context given there
                androidContext(this@CustomApplication)
                // load properties from assets/koin.properties file
                androidFileProperties()
                // module list
                modules(
                    listOf(
                        viewModelModule,
                        useCaseModules,
                        databaseModule
                    )
                )
            }
        }
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        FirebaseApp.initializeApp(this);
    }




}