package com.example.currencyupdateapp.ui.currency.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyupdateapp.R
import com.example.currencyupdateapp.ui.BindableAdapter
import com.example.currencyupdateapp.util.CountriesCodes
import kotlinx.android.synthetic.main.currency_item.view.*


class CurrencyRVAdapter(
    private val itemClickCallback: ((String) -> Unit),
    private val afterTextChanged: ((String) -> Unit)
) :
    RecyclerView.Adapter<CurrencyRVAdapter.UserHolder>(),
    BindableAdapter<List<CurrencyItem>> {

    private var currencyItem = mutableListOf<CurrencyItem>()


    override fun setData(data: List<CurrencyItem>?) {
        if (data != null) {
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

        holder.itemView.currencyCount.setOnFocusChangeListener { _, b ->
            if (b) {
                itemClickCallback.invoke(currencyItem[holder.adapterPosition].currency)
            }
        }

        holder.itemView.currencyCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (holder.adapterPosition == 0) {
                    var res = s.toString()
                    if (res.isEmpty()) res = "0"
                    currencyItem[0].value = res.toFloatOrNull() ?: 0f
                    afterTextChanged.invoke(res)
                }
            }
        })

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