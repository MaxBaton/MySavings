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
import com.example.mysavings.app.presentation.viewModel.ReplenishmentViewModel
import com.example.mysavings.app.presentation.viewModel.RestViewModel
import com.example.mysavings.databinding.DialogAddAccumulationBinding
import com.example.mysavings.databinding.DialogRecyclerWithSimpleTextBinding
import com.example.mysavings.databinding.ItemAccumulationBinding
import com.example.mysavings.databinding.ItemSimpleTextBinding
import com.example.mysavings.domain.models.other.FailAccumulation
import com.example.mysavings.domain.models.other.FailReplenishment
import com.example.mysavings.domain.models.repository.Accumulation
import com.example.mysavings.domain.usecase.accumulation.CheckCorrectAccumulationData
import com.example.mysavings.domain.usecase.common.GetDateStr
import com.example.mysavings.domain.usecase.replenishment.CheckReplenishmentData
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem

fun AppCompatActivity.createListAccumulationsDialog(): AlertDialog {
    val binding = DialogRecyclerWithSimpleTextBinding.inflate(LayoutInflater.from(this))
    val groupieAdapter = GroupieAdapter()
    val section = Section()
    groupieAdapter.add(section)

    val accumulationsViewModel: AccumulationsViewModel by this.viewModels()
    val restViewModel: RestViewModel by this.viewModels()

    accumulationsViewModel.accumulationsLiveData.observe(this) { accumulations ->
        val accumulationItems = mutableListOf<AccumulationItem>()
        accumulations?.forEach {
            accumulationItems.add(AccumulationItem(accumulation = it))
        }
        section.update(accumulationItems)
    }


    groupieAdapter.setOnItemClickListener { item, view ->
        val accumulationItem = item as AccumulationItem
        val accumulation = accumulationItem.accumulation

        val replenishmentViewModel: ReplenishmentViewModel by this.viewModels()

        val (dialog, dialogBinding) = createAddReplenishmentDialog()
        dialog.setOnShowListener { dialogInterface ->
            val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            btnPositive.setOnClickListener {
                val sumStr = dialogBinding.etAddReplenishmentSum.text.toString()
                val checkData = CheckReplenishmentData(
                    sumStr = sumStr
                ).check()

                when(checkData) {
                    is FailReplenishment.EmptySum -> {
                        dialogBinding.etAddReplenishmentSum.requestFocus()
                        showShortToast(text = getString(checkData.stringMessageId))
                    }
                    is FailReplenishment.IncorrectSum -> {
                        dialogBinding.etAddReplenishmentSum.requestFocus()
                        showShortToast(text = getString(checkData.stringMessageId))
                    }
                    is FailReplenishment.AllOk -> {
                        val sumFloat = sumStr.trim().toFloat()
                        replenishmentViewModel.addReplenishment(
                            sum = sumFloat,
                            date = dialogBinding.etAddReplenishmentDate.text.toString().trim(),
                            idAccumulation = accumulation.id,
                            onSuccess = {
                                accumulationsViewModel.addReplenishmentForAccumulation(
                                    sumAdd = sumFloat,
                                    accumulation = accumulation,
                                    onSuccess = {
                                        restViewModel.addAdditionalRest(additionalRest = sumFloat, isAdd = false)
                                        dialogInterface.dismiss()
                                    },
                                    onError = {
                                        showShortToast(text = getString(R.string.toast_error_add_accumulation))
                                    }
                                )
                            },
                            onError = {
                                showShortToast(text = getString(R.string.toast_error_add))
                            }
                        )
                    }
                }
            }
        }
        dialog.show()
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