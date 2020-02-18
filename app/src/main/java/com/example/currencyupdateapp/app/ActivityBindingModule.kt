package com.example.currencyupdateapp.app

import com.example.currencyupdateapp.dagger.ActivityScope
import com.example.currencyupdateapp.ui.currency.CurrencyModuleActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [CurrencyModule::class])
    fun  contributesCurrencyModuleActivity(): CurrencyModuleActivity
}