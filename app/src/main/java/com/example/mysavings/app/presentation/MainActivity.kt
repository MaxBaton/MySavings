package com.example.mysavings.app.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysavings.R
import com.example.mysavings.app.presentation.common.dialogs.createAddEdExpenditureDialog
import com.example.mysavings.app.presentation.common.dialogs.createAddEdRestDialog
import com.example.mysavings.app.presentation.common.dialogs.createAddModeDialog
import com.example.mysavings.app.presentation.common.showShortToast
import com.example.mysavings.app.presentation.common.toggleKeyboard
import com.example.mysavings.app.presentation.viewModel.ExpenditureViewModel
import com.example.mysavings.app.presentation.viewModel.RestViewModel
import com.example.mysavings.data.data.DefaultValues
import com.example.mysavings.data.data.EnumRestDialogMode
import com.example.mysavings.databinding.ActivityMainBinding
import com.example.mysavings.databinding.ItemExpenditureBinding
import com.example.mysavings.databinding.ItemExpenditureHeaderBinding
import com.example.mysavings.domain.models.repository.Expenditure
import com.example.mysavings.domain.usecase.expenditure.CheckCorrectExpenditureData
import com.example.mysavings.domain.usecase.expenditure.EnumFailExpenditureData
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val restViewModel: RestViewModel by viewModels()
    private val expenditureViewModel: ExpenditureViewModel by viewModels()
    // Expenditure Recycler
    private val groupieAdapter = GroupieAdapter()
    private val section = Section()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            section.apply {
                setHeader(ExpenditureHeader())
                setHideWhenEmpty(true)
            }
            groupieAdapter.add(section)
            recyclerViewExpenses.apply {
                adapter = groupieAdapter
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            }

            restViewModel.restStrLiveData.observe(this@MainActivity) { restStr ->
                textViewRest.text = restStr
            }

            expenditureViewModel.expensesLiveData.observe(this@MainActivity) { expenses ->
                val expenditureItems = mutableListOf<ExpenditureItem>()
                expenses?.forEach {
                    expenditureItems.add(ExpenditureItem(expenditure = it))
                }
                section.update(expenditureItems)
            }

            fabAdd.setOnClickListener {
                val dialog = this@MainActivity.createAddModeDialog()
                dialog.show()
            }

            fabEd.setOnClickListener {
                val (dialog, dialogBinding) = this@MainActivity.createAddEdRestDialog(mode = EnumRestDialogMode.EDIT.restDialogMode)
                dialog.setOnShowListener { dialogInterface ->
                    this@MainActivity.toggleKeyboard()

                    val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btnPositive.setOnClickListener {
                        val restStr = dialogBinding.etAddRest.text.toString().trim()
                        if (restStr == "") {
                            this@MainActivity.showShortToast(text = getString(R.string.toast_blank_rest))
                            return@setOnClickListener
                        }else {
                            val restFloat = restStr.toFloat()
                            restViewModel.changeRest(newRest = restFloat)
                            dialogInterface.dismiss()
                        }
                    }
                }
                dialog.show()
            }

            groupieAdapter.setOnItemClickListener { item, view ->
                val checkCorrectExpenditureData = CheckCorrectExpenditureData()

                val expenditureItem = item as ExpenditureItem
                val expenditure = expenditureItem.expenditure
                val (dialog, dialogBinding) = createAddEdExpenditureDialog(expenditure = expenditure)
                dialog.setOnShowListener { dialogInterface ->
                    val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btnPositive.setOnClickListener {
                        val newSumStr = dialogBinding.etAddMoneyExpensesSum.text.toString().trim()
                        val newDescription = dialogBinding.etAddMoneyExpensesDescription.text.toString().trim()
                        val newDate = dialogBinding.etAddMoneyExpensesDate.text.toString().trim()
                        val currRest = restViewModel.getCurrRest()
                        val oldSum = expenditure.sum

                        val isCorrectData = checkCorrectExpenditureData.checkEditable(
                            newSumStr = newSumStr,
                            oldSum = oldSum,
                            currRest = currRest
                        )

                        when(isCorrectData) {
                            EnumFailExpenditureData.INCORRECT_SUM -> {
                                this@MainActivity.showShortToast(text = getString(isCorrectData.failExpenditureData.stringMessageId))
                                dialogBinding.etAddMoneyExpensesSum.requestFocus()
                                return@setOnClickListener
                            }
                            EnumFailExpenditureData.TOO_BIG_EXPENDITURE -> {
                                this@MainActivity.showShortToast(text = getString(isCorrectData.failExpenditureData.stringMessageId))
                                dialogBinding.etAddMoneyExpensesSum.requestFocus()
                                return@setOnClickListener
                            }
                            EnumFailExpenditureData.ALL_OK -> {
                                val newSumFloat = newSumStr.toFloat()
                                expenditure.sum = newSumFloat
                                expenditure.date = newDate
                                expenditure.description = newDescription

                                expenditureViewModel.edExpenditure(
                                    expenditure = expenditure,
                                    oldSum = oldSum,
                                    onSuccess = {
                                        editCurrRest(newSumFloat, oldSum)
                                    },
                                    onError = {
                                        this@MainActivity.showShortToast(text = getString(R.string.toast_error_edit))
                                    }
                                )
                                dialogInterface.dismiss()
                            }
                        }
                    }
                }
                dialog.show()
            }
        }
    }

    private fun editCurrRest(newSumFloat: Float, oldSum: Float) {
        val currRest = restViewModel.getCurrRest()
        val newRest = if (newSumFloat > oldSum) {
            currRest - (newSumFloat - oldSum)
        }else {
            currRest + (oldSum - newSumFloat)
        }
        restViewModel.changeRest(newRest = newRest)
    }

    inner class ExpenditureItem(val expenditure: Expenditure): BindableItem<ItemExpenditureBinding>() {
        override fun bind(viewBinding: ItemExpenditureBinding, position: Int) {
            with(viewBinding) {
                textViewSum.text = expenditure.sum.toString()
                textViewDate.text = expenditure.date
                textViewDescription.text = expenditure.description
            }
        }

        override fun getLayout() = R.layout.item_expenditure
        override fun initializeViewBinding(view: View) = ItemExpenditureBinding.bind(view)
    }

    inner class ExpenditureHeader(): BindableItem<ItemExpenditureHeaderBinding>() {
        override fun bind(viewBinding: ItemExpenditureHeaderBinding, position: Int) {}
        override fun getLayout() = R.layout.item_expenditure_header
        override fun initializeViewBinding(view: View) = ItemExpenditureHeaderBinding.bind(view)
    }
}