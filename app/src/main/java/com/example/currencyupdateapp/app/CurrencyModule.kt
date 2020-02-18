package com.example.currencyupdateapp.app

import com.example.currencyupdateapp.dagger.FragmentScope
import com.example.currencyupdateapp.ui.currency.CurrencyListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface CurrencyModule {
    @FragmentScope
    @ContributesAndroidInjector
    fun  contributesCurrencyListfragment(): CurrencyListFragment

}