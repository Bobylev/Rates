package com.example.currencyupdateapp.network

import com.example.currencyupdateapp.network.data.CurrencyResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiAccess {
@GET("latest?base=EUR")
fun getCurrency() : Observable<CurrencyResponse>
}