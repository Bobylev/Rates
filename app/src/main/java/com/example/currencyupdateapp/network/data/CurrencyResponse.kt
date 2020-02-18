package com.example.currencyupdateapp.network.data

import com.google.gson.annotations.SerializedName

data class CurrencyResponse(
    @SerializedName("baseCurrency") val currency : String,
    @SerializedName("rates") var rates : MutableMap<String, Float>
)
