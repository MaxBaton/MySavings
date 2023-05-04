package com.example.mysavings.app.presentation.common.dialogs

import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysavings.R
import com.example.mysavings.app.presentation.common.showShortToast
import com.example.mysavings.app.presentation.viewModel.AccumulationsViewModel
import com.example.mysavings.databinding.DialogAddAccumulationBinding
import com.example.mysavings.databinding.DialogRecyclerWithSimpleTextBinding
import com.example.mysavings.databinding.ItemAccumulationBinding
import com.example.mysavings.databinding.ItemSimpleTextBinding
import com.example.mysavings.domain.models.other.FailAccumulation
import com.example.mysavings.domain.models.repository.Accumulation
import com.example.mysavings.domain.usecase.accumulation.CheckCorrectAccumulationData
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem

fun AppCompatActivity.createListAccumulationsDialog(): AlertDialog {
    val binding = DialogRecyclerWithSimpleTextBinding.inflate(LayoutInflater.from(this))
    val groupieAdapter = GroupieAdapter()
    val section = Section()
    groupieAdapter.add(section)

    groupieAdapter.setOnItemClickListener { item, view ->
        val accumulationItem = item as AccumulationItem
        val accumulation = accumulationItem.accumulation

    }

    val accumulationsViewModel: AccumulationsViewModel by this.viewModels()
    accumulationsViewModel.accumulationsLiveData.observe(this) { accumulations ->
        val accumulationItems = mutableListOf<AccumulationItem>()
        accumulations?.forEach {
            accumulationItems.add(AccumulationItem(accumulation = it))
        }
        section.update(accumulationItems)
    }

    binding.recyclerView.apply {
        adapter = groupieAdapter
        layoutManager = LinearLayoutManager(this@createListAccumulationsDialog, LinearLayoutManager.VERTICAL, false)
        addItemDecoration(DividerItemDecoration(this@createListAccumulationsDialog, DividerItemDecoration.VERTICAL))
    }


    val dialog = AlertDialog.Builder(this).apply {
        setView(binding.root)
        setTitle(getString(R.string.dialog_accumulation_list_title))
        setPositiveButton(getString(R.string.dialog_btn_add), null)
        setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
    }.create()

    return dialog
}

fun AppCompatActivity.createAddAccumulationDialog(): Pair<AlertDialog, DialogAddAccumulationBinding> {
    val binding = DialogAddAccumulationBinding.inflate(LayoutInflater.from(this))
    val dialog = AlertDialog.Builder(this).apply {
        setTitle(getString(R.string.dialog_add_accumulation_title))
        setView(binding.root)
        setPositiveButton(getString(R.string.dialog_btn_add), null)
        setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
    }.create()

    return dialog to binding
}

class AccumulationItem(val accumulation: Accumulation): BindableItem<ItemAccumulationBinding>() {
    override fun bind(viewBinding: ItemAccumulationBinding, position: Int) {
        with(viewBinding) {
            textViewName.text = accumulation.name
            textViewSum.text = accumulation.sum.toString()
        }
    }


    override fun getLayout() = R.layout.item_accumulation

    override fun initializeViewBinding(view: View) = ItemAccumulationBinding.bind(view)

}