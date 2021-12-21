package com.nakib.iamhere.networking

import com.nakib.iamhere.MainActivityViewModel
import com.nakib.iamhere.ui.addNewTime.AddNewTimeViewModel
import com.nakib.iamhere.ui.admin.AdminViewModel
import com.nakib.iamhere.ui.homeNormal.HomeNormalViewModel
import com.nakib.iamhere.ui.login.LoginViewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.koin.android.viewmodel.dsl.viewModel

object ApiClient {

    private const val BASE_URL_HOME = "http://masry.live/app/json/"


    val networkModule = module {
        fun makeRetrofitService(): AppRequests {
            return Retrofit.Builder()
                .baseUrl(BASE_URL_HOME)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(AppRequests::class.java)
        }

        single { makeRetrofitService() }
    }

    val loginModule = module { viewModel { LoginViewModel(get()) } }
    val addNewTimeModule = module { viewModel { AddNewTimeViewModel(get()) } }
    val normalHomeModule = module { viewModel { HomeNormalViewModel(get()) } }
    val mainActivityModule = module { viewModel { MainActivityViewModel(get()) } }
    val adminModule = module { viewModel { AdminViewModel(get()) } }



}