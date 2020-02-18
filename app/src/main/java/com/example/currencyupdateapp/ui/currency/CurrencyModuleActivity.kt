package com.example.currencyupdateapp.ui.currency

import android.os.Bundle
import com.example.currencyupdateapp.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.currency_activity.*


class CurrencyModuleActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.currency_activity)

        setSupportActionBar(toolbar)

        //val actionBar: ActionBar? = supportActionBar
        //actionBar?.setTitle(R.string.currency_screen)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CurrencyListFragment.newInstance())
                .commitNow()
        }
    }

}
