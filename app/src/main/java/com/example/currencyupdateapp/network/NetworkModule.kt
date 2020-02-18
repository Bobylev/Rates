package com.example.currencyupdateapp.network

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun providesClient() : ApiAccess{
        val retrofit = Retrofit.Builder()
            .baseUrl("https://hiring.revolut.codes/api/android/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return  retrofit.create<ApiAccess>(ApiAccess::class.java)
    }
}