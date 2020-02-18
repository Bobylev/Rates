package com.example.currencyupdateapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    inline fun <reified T : ViewModel> BaseFragment.getViewModel(): T {
        return ViewModelProvider(this, viewModelFactory)[T::class.java]
    }

}
