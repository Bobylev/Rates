package com.example.currencyupdateapp.model

import androidx.lifecycle.LiveData
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

    private val mainCurrency = "EUR"

    private val _errorData: MutableLiveData<String> = MutableLiveData()
    val errorData: LiveData<String> = _errorData

    private val _currencyRunner: MutableLiveData<Boolean> = MutableLiveData(false)

    val currenciesData: MutableLiveData<MutableList<CurrencyItem>> = MutableLiveData()
    private var dataMap = emptyMap<String, Float>()
    private var newTopValue = 0f

    fun setNewTop(curr: String) {
        val list = currenciesData.value?.toMutableList()
        if (list != null) {
            val index = list.indexOf(list.find { currencyItem -> currencyItem.currency == curr })
            if(index  != -1) {
                list.add(0, list[index])
                list.removeAt(index + 1)
                newTopValue = list[0].value
                currenciesData.postValue(list)
            }
        }
    }

    fun setNewCoefficient(newValue: String) {
        newTopValue = if (newValue.isEmpty()) 0f else newValue.toFloat()
        val list = currenciesData.value?.toMutableList()
        list?.get(0)?.value = newTopValue
        newData(list, getCoefficient())
    }

    private fun getCoefficient(): Float {
        val list = currenciesData.value?.toMutableList()
        var result = 1f
        if (list != null) {
            result = newTopValue / (dataMap[list[0].currency] ?: 1f)
        }
        return result
    }

    @Synchronized
    private fun newData(list: MutableList<CurrencyItem>?, coefficient: Float) {
        for (item in dataMap) {
            val res =
                list?.find { currencyItem -> currencyItem.currency == item.key }
            if (res != null) {
                if (list.indexOf(res) != 0) {
                    res.value = round(item.value * coefficient * 100) / 100
                }
            } else {
                list?.add(
                    CurrencyItem(
                        item.key,
                        round(item.value * coefficient * 100) / 100
                    )
                )
            }
        }
        list?.removeAll { currencyItem -> (currencyItem.currency != mainCurrency) && (dataMap[currencyItem.currency] == null) }
        currenciesData.postValue(list)
    }

    fun getBalances() {
        if (_currencyRunner.value == false) {
            _currencyRunner.postValue(true)
            disposable.add(Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .switchMap { api.getCurrency() }
                .observeOn(Schedulers.io())
                .retry { e ->
                    (true.also {
                        _errorData.postValue(e.localizedMessage)
                        Thread.sleep(2000)
                    })
                }
                .map {
                    _errorData.postValue("OK")
                    it.rates[mainCurrency] = 1f
                    return@map it
                }
                .subscribeBy(
                    onNext = {
                        dataMap = it.rates
                        val list: MutableList<CurrencyItem>? =
                            currenciesData.value ?: mutableListOf()
                        if (dataMap.isNotEmpty()) {
                            newData(list, getCoefficient())
                        }
                    },
                    onError = {
                        _currencyRunner.postValue(false)
                    },
                    onComplete = {
                        _currencyRunner.postValue(false)
                    }
                )
            )
        }
    }
}
