package com.example.currencyupdateapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.currencyupdateapp.network.ApiAccess
import com.example.currencyupdateapp.ui.currency.adapter.CurrencyItem
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.round

class CurrencyViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var api: ApiAccess

    val currenciesData: MutableLiveData<MutableList<CurrencyItem>> = MutableLiveData()
    private var dataMap = emptyMap<String, Float>()
    private var newTopValue = 0f

    fun setNewTop(curr: String) {
        val list = currenciesData.value?.toMutableList()
        if (list != null) {
            val index = list.indexOf(list.find { currencyItem -> currencyItem.currency == curr })
            list[index] = list[0].also { list[0] = list[index] }
        }
        newTopValue = list?.get(0)?.value ?: 1f
        currenciesData.postValue(list)
    }

    fun setNewCoeficient(newValue: String) {
        newTopValue = if (newValue.isEmpty()) 0f else newValue.toFloat()
        val list = currenciesData.value?.toMutableList()
        list?.get(0)?.value = newTopValue

        Log.i("resultx", newValue)
    }

    private fun getCoefficient(): Float {
        val list = currenciesData.value?.toMutableList()
        var result = 1f
        if (list != null) {
            result = newTopValue / (dataMap[list[0].currency] ?: 1f)
        }
        return result
    }

    fun getBalances() {

        disposible.add(Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .switchMap { api.getCurrency() }
            .observeOn(Schedulers.io())
            .retry{ e -> ( true.also {
                Thread.sleep(2000)
                Log.i("resultx", "wait" + e.localizedMessage )
            } ) }
            .map {
                it.rates["EUR"] = 1f
                return@map it
            }
            .subscribeBy(
                onNext = {
                    dataMap = it.rates
                    val list: MutableList<CurrencyItem>? = currenciesData.value ?: mutableListOf()

                    val coef = getCoefficient()
                    if (dataMap.isNotEmpty()) {
                        for (item in dataMap) {
                            val res =
                                list?.find { currencyItem -> currencyItem.currency == item.key }
                            if (res != null) {
                                if (list.indexOf(res) != 0) {
                                    res.value = round(item.value * coef *100)/100
                                }
                            } else {
                                list?.add(CurrencyItem(item.key, round(item.value * coef *100)/100))
                            }
                        }

                        //newTopValue = list?.get(0)?.value ?: 0f

                        currenciesData.postValue(list)
                    }

                    Log.i("resultx", "OK")
                },
                onError = {
                    Log.i("resultx", it.localizedMessage ?: "error")
                }
            )
        )
    }
}
