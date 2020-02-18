package com.example.currencyupdateapp.model

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel: ViewModel() {

    val disposible = CompositeDisposable()

    override fun onCleared() {
        disposible.clear()
        super.onCleared()
    }

}