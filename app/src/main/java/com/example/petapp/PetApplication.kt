package com.example.petapp

import android.app.Application
import com.example.petapp.di.dataModule
import com.example.petapp.di.domainModule
import com.example.petapp.di.featureModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PetApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PetApplication)
            modules(
                dataModule,
                domainModule,
                featureModule
            )
        }
    }
}
