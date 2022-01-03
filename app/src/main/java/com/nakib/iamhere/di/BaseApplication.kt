package com.nakib.iamhere.di

import android.app.Application
import android.content.Context
import com.nakib.iamhere.networking.ApiClient.addNewTimeModule
import com.nakib.iamhere.networking.ApiClient.adminModule
import com.nakib.iamhere.networking.ApiClient.doctorLocationModule
import com.nakib.iamhere.networking.ApiClient.loginModule
import com.nakib.iamhere.networking.ApiClient.mainActivityModule
import com.nakib.iamhere.networking.ApiClient.networkModule
import com.nakib.iamhere.networking.ApiClient.normalHomeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {

    init {
        instance = this
    }

    companion object{
        private var instance : BaseApplication? = null
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context=BaseApplication.applicationContext()

        startKoin {

            androidContext(this@BaseApplication)
            modules(listOf(
                networkModule,
                loginModule,
                addNewTimeModule,
                normalHomeModule,
                mainActivityModule,
                adminModule,
                doctorLocationModule
            ))
        }
    }
}