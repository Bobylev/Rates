package com.example.currencyupdateapp.ui

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment() {

    var snackbar: Snackbar? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    inline fun <reified T : ViewModel> getViewModel(): T {
        return ViewModelProvider(this, viewModelFactory)[T::class.java]
    }


    fun showError(msg: String){
        if(msg != "OK") {
            if(view != null) {
                if (snackbar == null) {
                    snackbar = Snackbar.make(view!!, msg, Snackbar.LENGTH_INDEFINITE)
                    snackbar?.setActionTextColor(Color.WHITE)
                    snackbar?.view?.setBackgroundColor(Color.RED)
                    snackbar?.show()
                } else {
                    snackbar?.setText(msg)
                }
            }
        } else {
            snackbar?.dismiss()
        }
    }
}
