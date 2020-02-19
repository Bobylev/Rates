package com.example.currencyupdateapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currencyupdateapp.model.CurrencyViewModel
import com.example.currencyupdateapp.network.ApiAccess
import com.example.currencyupdateapp.network.data.CurrencyResponse
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Observable
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CurrencyViewModelUnitTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()
    private val api = mock<ApiAccess> {
        on { getCurrency() } doReturn Observable.just(CurrencyResponse("EUR", mutableMapOf("USD" to 1.598f, "BGN" to 1.97f,"BRL" to 4.238f,"CAD"  to 1.516f,"CHF" to 1.145f, "CNY" to 7.721f,
            "CZK" to 25.76f,"DKK" to 7.593f,"GBP" to 0.885f,"HKD" to 9.035f,"HRK" to 7.471f,"HUF"  to 320.548f,"IDR"  to 16113.475f,"ILS" to 4.164f,"INR" to 82.136f,"ISK" to 134.611f,"JPY" to 126.737f)))
    }

    @Test
    fun `set new top to currencies`()
    {
        val currencyViewModel = CurrencyViewModel()
        currencyViewModel.api = api
        currencyViewModel.getBalances()
        Thread.sleep(2000)
        currencyViewModel.setNewTop("USD")
        assert(currencyViewModel.currenciesData.value != null)
        assert(currencyViewModel.currenciesData.value!![0].currency == "USD")
    }

    @Test
    fun `set new coefficient`()
    {
        val currencyViewModel = CurrencyViewModel()
        currencyViewModel.api = api
        currencyViewModel.getBalances()
        Thread.sleep(2000)
        currencyViewModel.setNewCoefficient("12")
        assert(currencyViewModel.currenciesData.value != null)
        assert(currencyViewModel.currenciesData.value!![0].value == 12f)
    }


    @Test
    fun `set deleted currency to top`()
    {
        val currencyViewModel = CurrencyViewModel()
        currencyViewModel.api = api
        currencyViewModel.getBalances()
        Thread.sleep(2000)
        currencyViewModel.setNewTop("RED")
        assert(currencyViewModel.currenciesData.value != null)
        assert(currencyViewModel.currenciesData.value!![0].value == 1.6f)
    }

    @Test
    fun `set new zero coefficient`()
    {
        val currencyViewModel = CurrencyViewModel()
        currencyViewModel.api = api
        currencyViewModel.getBalances()
        Thread.sleep(2000)
        currencyViewModel.setNewCoefficient("0")
        assert(currencyViewModel.currenciesData.value != null)
        assert(currencyViewModel.currenciesData.value!![0].value == 0f)
    }

    @Test
    fun `set new zero coefficient check another currencies to zero`()
    {
        val currencyViewModel = CurrencyViewModel()
        currencyViewModel.api = api
        currencyViewModel.getBalances()
        Thread.sleep(2000)
        currencyViewModel.setNewCoefficient("0")
        assert(currencyViewModel.currenciesData.value != null)
        assert(currencyViewModel.currenciesData.value!![1].value == 0f)
        assert(currencyViewModel.currenciesData.value!![2].value == 0f)
    }

}
