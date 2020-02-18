package com.example.currencyupdateapp.app

import com.example.currencyupdateapp.network.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, ViewModelModule::class, ActivityBindingModule::class, NetworkModule::class])
interface AppComponent : AndroidInjector<DaggerApplication>