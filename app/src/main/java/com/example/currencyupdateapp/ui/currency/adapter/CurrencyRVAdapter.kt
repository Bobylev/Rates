package com.example.currencyupdateapp.ui.currency.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyupdateapp.R
import com.example.currencyupdateapp.ui.BindableAdapter
import com.example.currencyupdateapp.util.CountriesCodes
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.currency_item.view.*
import java.util.concurrent.TimeUnit


class CurrencyRVAdapter(
    private val itemClickCallback: ((String) -> Unit),
    private val afterTextChanged: ((String) -> Unit)
) :
    RecyclerView.Adapter<CurrencyRVAdapter.UserHolder>(),
    BindableAdapter<List<CurrencyItem>> {

    private var currencyItem = mutableListOf<CurrencyItem>()

    override fun setData(data: List<CurrencyItem>?) {
        if (data != null) {
            Log.i("result", "redraw")

            if (currencyItem.isNotEmpty()) {
                if (data[0].currency == currencyItem[0].currency) {
                    data[0].value = currencyItem[0].value
                }
            }
            notifyChanges(data.toMutableList())
        }
    }

    private fun notifyChanges(newList: List<CurrencyItem>) {

        val oldList = mutableListOf<CurrencyItem>()
        currencyItem.forEach {
            oldList.add(CurrencyItem(it.currency, it.value))
        }

        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].currency == newList[newItemPosition].currency
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].value == newList[newItemPosition].value
            }

            override fun getOldListSize() = oldList.size
            override fun getNewListSize() = newList.size
        })

        currencyItem.clear()
        newList.forEach {
            currencyItem.add(CurrencyItem(it.currency, it.value))
        }

        diff.dispatchUpdatesTo(this)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserHolder(
            inflater.inflate(R.layout.currency_item, parent, false)
        )
    }

    override fun getItemCount() = currencyItem.size

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                holder.itemView.currencyCount.requestFocus()
                itemClickCallback.invoke(currencyItem[holder.adapterPosition].currency)
            }
        }

        //if (holder.adapterPosition == 0) {
            val res = RxTextView.afterTextChangeEvents(holder.itemView.currencyCount)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                    if (it.editable().toString().isEmpty()) {
                        holder.itemView.currencyCount.setText("0")
                        return@subscribe
                    }

                    var res = it.editable().toString().trimStart('0')
                    if (it.editable().toString() != "0") {
                        if (it.editable().toString() != it.editable().toString().trimStart('0')) {
                            if(res.isEmpty()) res = "0"
                            holder.itemView.currencyCount.setText(res)
                        }
                    }


                    if (holder.adapterPosition == 0) {
                        if(res.isEmpty()) res = "0"
                        currencyItem[0].value = res.toFloat()
                        afterTextChanged.invoke(res)
                    }
          //      }
        }

        holder.bind(currencyItem[position])
    }

    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val countryDescription = CountriesCodes()

        fun bind(currencyItem: CurrencyItem) {
            if (itemView.countryText.text != currencyItem.currency) {
                itemView.countryImage.setImageResource(
                    countryDescription.countriesImageMap[currencyItem.currency] ?: R.drawable.usa
                )
                itemView.countryText.text = currencyItem.currency
                itemView.countryDescription.text =
                    countryDescription.countriesCodesMap[currencyItem.currency]
                        ?: currencyItem.currency
            }
            if (itemView.currencyCount.text.toString() != currencyItem.value.toString()) {
                itemView.currencyCount.setText(currencyItem.value.toString())
            }
        }
    }
}