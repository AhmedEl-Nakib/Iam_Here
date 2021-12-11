package com.example.iamhere.di

import android.app.Application
import com.example.iamhere.networking.ApiClient.addNewTimeModule
import com.example.iamhere.networking.ApiClient.loginModule
import com.example.iamhere.networking.ApiClient.mainActivityModule
import com.example.iamhere.networking.ApiClient.networkModule
import com.example.iamhere.networking.ApiClient.normalHomeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {

            androidContext(this@BaseApplication)
            modules(listOf(
                networkModule,
                loginModule,
                addNewTimeModule,
                normalHomeModule,
                mainActivityModule
            ))
        }
    }
}