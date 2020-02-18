package com.example.currencyupdateapp.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

interface BindableAdapter<T> {
    fun setData(data: T?)
}
   @BindingAdapter("data")
    fun <T> setRecyclerViewProperties(recyclerView: RecyclerView, data: T) {
        if (recyclerView.adapter is BindableAdapter<*>) {
            (recyclerView.adapter as BindableAdapter<T>).setData(data)
        }
    }
