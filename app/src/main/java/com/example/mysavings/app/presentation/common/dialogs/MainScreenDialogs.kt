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
import com.example.mysavings.app.presentation.viewModel.ExpenditureViewModel
import com.example.mysavings.app.presentation.viewModel.RestViewModel
import com.example.mysavings.data.data.EnumAddMode
import com.example.mysavings.data.data.EnumRestDialogMode
import com.example.mysavings.databinding.DialogRecyclerWithSimpleTextBinding
import com.example.mysavings.databinding.ItemSimpleTextBinding
import com.example.mysavings.domain.models.other.FailAccumulation
import com.example.mysavings.domain.models.other.FailExpenditure
import com.example.mysavings.domain.usecase.accumulation.AccumulationOperations
import com.example.mysavings.domain.usecase.accumulation.CheckCorrectAccumulationData
import com.example.mysavings.domain.usecase.expenditure.CheckCorrectExpenditureData
import com.example.mysavings.domain.usecase.main.QueueDialogs
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem

fun AppCompatActivity.createAddModeDialog(queueDialogs: QueueDialogs): AlertDialog {
    val binding = DialogRecyclerWithSimpleTextBinding.inflate(LayoutInflater.from(this))
    val groupieAdapter = GroupieAdapter()
    val section = Section()

    val restViewModel: RestViewModel by viewModels()

    EnumAddMode.values().forEach {
        section.add(AddModeItem(
            activity = this,
            enumAddMode = it
        ))
    }

    groupieAdapter.add(section)

    binding.recyclerView.apply {
        layoutManager = LinearLayoutManager(this@createAddModeDialog, LinearLayoutManager.VERTICAL, false)
        addItemDecoration(DividerItemDecoration(this@createAddModeDialog, DividerItemDecoration.VERTICAL))
        adapter = groupieAdapter
    }

    groupieAdapter.setOnItemClickListener { item, view ->
        val addModeItem = item as AddModeItem
        when(addModeItem.enumAddMode) {
            EnumAddMode.REST -> {
                val (dialog, dialogBinding) = this.createAddEdRestDialog(mode = EnumRestDialogMode.ADD.restDialogMode)
                dialog.setOnShowListener { dialogInterface ->
                    val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btnPositive.setOnClickListener {
                        val restStr = dialogBinding.etAddRest.text.toString()
                        if (restStr.isBlank()) {
                            this.showShortToast(text = getString(R.string.toast_blank_rest))
                            return@setOnClickListener
                        }else {
                            val restFloat = restStr.trim().toFloat()
                            restViewModel.addAdditionalRest(additionalRest = restFloat)
                            dialogInterface.dismiss()
                        }
                    }
                }
                dialog.show()
                queueDialogs.add(dialog = dialog)
            }
            EnumAddMode.EXPENSES -> {
                val expenditureViewModel: ExpenditureViewModel by this.viewModels()
                val checkCorrectExpenditureData = CheckCorrectExpenditureData()

                val (dialog, dialogBinding) = this.createAddEdExpenditureDialog()
                dialog.setOnShowListener { dialogInterface ->
                    val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btnPositive.setOnClickListener {
                        val sumStr = dialogBinding.etAddMoneyExpensesSum.text.toString().trim()
                        val description = dialogBinding.etAddMoneyExpensesDescription.text.toString().trim()
                        val date = dialogBinding.etAddMoneyExpensesDate.text.toString().trim()
                        val currRest = restViewModel.getCurrRest()

                        val isCorrectData = checkCorrectExpenditureData.check(
                            sumStr = sumStr,
                            currRest = currRest
                        )

                        when (isCorrectData) {
                            is FailExpenditure.IncorrectSum -> {
                                this.showShortToast(text = getString(isCorrectData.stringMessageId))
                                dialogBinding.etAddMoneyExpensesSum.requestFocus()
                                return@setOnClickListener
                            }
                            is FailExpenditure.TooBigExpenditure -> {
                                this.showShortToast(text = getString(isCorrectData.stringMessageId))
                                dialogBinding.etAddMoneyExpensesSum.requestFocus()
                                return@setOnClickListener
                            }
                            is FailExpenditure.AllOk -> {
                                val sumFloat = sumStr.toFloat()
                                expenditureViewModel.addExpenditure(
                                    sum = sumFloat,
                                    description = description,
                                    date = date,
                                    onSuccess = {
                                        updateCurrRest(
                                            restViewModel = restViewModel,
                                            expenditureSum = sumFloat
                                        )
                                    },
                                    onError = {
                                        this.showShortToast(text = getString(R.string.toast_error_add))
                                    }
                                )
                                dialogInterface.dismiss()
                            }
                        }
                    }
                }
                dialog.show()
                queueDialogs.add(dialog = dialog)
            }
            EnumAddMode.ACCUMULATIONS -> {
                val accumulationsViewModel: AccumulationsViewModel by this.viewModels()
                accumulationsViewModel.setCurrOperations(operation = AccumulationOperations.GetAll)

                val dialog = this.createListAccumulationsDialog(queueDialogs = queueDialogs)
                dialog.setOnShowListener { dialogInterface ->
                    val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btnPositive.setOnClickListener {
                        val (dialog, dialogBinding) = this.createAddEdAccumulationDialog()
                        dialog.setOnShowListener { dialogInterfaceAdd ->
                            val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            btnPositive.setOnClickListener {
                                val name = dialogBinding.etAddAccumulationName.text.toString()
                                val sumStr = dialogBinding.etAddAccumulationSum.text.toString()

                                val checkData = CheckCorrectAccumulationData().check(
                                    name = name,
                                    sumStr = sumStr
                                )

                                when(checkData) {
                                    is FailAccumulation.EmptySum -> {
                                        dialogBinding.etAddAccumulationSum.requestFocus()
                                        showShortToast(text = getString(checkData.stringMessageId))
                                    }
                                    is FailAccumulation.IncorrectSum -> {
                                        dialogBinding.etAddAccumulationSum.requestFocus()
                                        showShortToast(text = getString(checkData.stringMessageId))
                                    }
                                    is FailAccumulation.EmptyName -> {
                                        dialogBinding.etAddAccumulationName.requestFocus()
                                        showShortToast(text = getString(checkData.stringMessageId))
                                    }
                                    is FailAccumulation.AllOk -> {
                                        accumulationsViewModel.addAccumulation(
                                            name = name,
                                            sum = sumStr.toFloat(),
                                            onSuccess = {
                                                showShortToast(text = getString(R.string.toast_successful_add))
                                                dialogInterfaceAdd.dismiss()
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
                        queueDialogs.add(dialog = dialog)
                    }
                }
                dialog.show()
                queueDialogs.add(dialog = dialog)
            }
        }
    }

    val dialog = AlertDialog.Builder(this).apply {
        setCancelable(false)
        setTitle(this.context.getString(R.string.dialog_add_modes_title))
        setView(binding.root)
        setPositiveButton("ok") { dialogInterface, _ -> dialogInterface.dismiss() }
    }.create()

    return dialog
}

private fun updateCurrRest(restViewModel: RestViewModel, expenditureSum: Float) {
    val currRest = restViewModel.getCurrRest()
    val newRest = currRest - expenditureSum
    restViewModel.changeRest(newRest = newRest)
}

class AddModeItem(
    private val activity: AppCompatActivity,
    val enumAddMode: EnumAddMode
): BindableItem<ItemSimpleTextBinding>() {
    override fun bind(viewBinding: ItemSimpleTextBinding, position: Int) {
        with(viewBinding) {
            textView.text = activity.getString(enumAddMode.addMode.nameStringId)
        }
    }

    override fun getLayout() = R.layout.item_simple_text

    override fun initializeViewBinding(view: View) = ItemSimpleTextBinding.bind(view)
}