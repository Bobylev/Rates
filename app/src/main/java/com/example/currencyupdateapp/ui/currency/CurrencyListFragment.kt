package com.example.currencyupdateapp.ui.currency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.currencyupdateapp.R
import com.example.currencyupdateapp.databinding.CurrencyFragmentBinding
import com.example.currencyupdateapp.model.CurrencyViewModel
import com.example.currencyupdateapp.ui.BaseFragment
import com.example.currencyupdateapp.ui.currency.adapter.CurrencyRVAdapter
import kotlinx.android.synthetic.main.currency_fragment.*


class CurrencyListFragment : BaseFragment() {

    companion object {
        fun newInstance() = CurrencyListFragment()
    }

    private lateinit var viewModel: CurrencyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding: CurrencyFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.currency_fragment, container, false)
        viewModel = getViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter =
            CurrencyRVAdapter(itemClickCallback = fun(index: String) {
                viewModel.setNewTop(index)
                currencyRecyclerView.scrollToPosition(0)
            },
                afterTextChanged = fun(newValue: String)  { viewModel.setNewCoeficient(newValue)})
        currencyRecyclerView.layoutManager = LinearLayoutManager(activity)
        currencyRecyclerView.adapter = adapter
        (currencyRecyclerView.itemAnimator  as SimpleItemAnimator).supportsChangeAnimations = false

        viewModel.getBalances()

    }

}
