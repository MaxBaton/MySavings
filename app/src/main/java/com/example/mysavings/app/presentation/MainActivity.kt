package com.example.mysavings.app.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysavings.R
import com.example.mysavings.app.presentation.common.dialogs.createAddEdExpenditureDialog
import com.example.mysavings.app.presentation.common.dialogs.createAddEdRestDialog
import com.example.mysavings.app.presentation.common.dialogs.createAddModeDialog
import com.example.mysavings.app.presentation.common.dialogs.createConfirmAlertDialog
import com.example.mysavings.app.presentation.common.showShortToast
import com.example.mysavings.app.presentation.common.toggleKeyboard
import com.example.mysavings.app.presentation.viewModel.AdditionalViewModel
import com.example.mysavings.app.presentation.viewModel.ExpenditureViewModel
import com.example.mysavings.app.presentation.viewModel.RestViewModel
import com.example.mysavings.data.data.EnumRestDialogMode
import com.example.mysavings.databinding.ActivityMainBinding
import com.example.mysavings.databinding.ItemExpenditureBinding
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
    private val additionalViewModel: AdditionalViewModel by viewModels()
    // Expenditure Recycler
    private val groupieAdapter = GroupieAdapter()
    private val section = Section()
    // Delete mode
    private var menu: Menu? = null
    private val deleteExpenditureList = mutableListOf<Expenditure>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        additionalViewModel.isDeleteModeLiveData.observe(this) { isDeleteMode ->
            menu?.findItem(R.id.action_delete)?.isVisible = isDeleteMode
            menu?.findItem(R.id.action_select_all)?.isVisible = isDeleteMode
            supportActionBar?.title = if (isDeleteMode) "" else getString(R.string.app_name)
        }

        with(binding) {
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
                if (expenses?.isEmpty() ?: true) {
                    hideRecyclerHeader()
                }else {
                    showRecyclerHeader()
                }

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
                val expenditureItem = item as ExpenditureItem
                val expenditure = expenditureItem.expenditure
                val position = section.getPosition(expenditureItem)

                if (additionalViewModel.getIsDeleteMode()) {
                    if (!expenditureItem.isDeleteMode) {
                        deleteExpenditureList.add(expenditure)
                    }else {
                        deleteExpenditureList.remove(expenditure)
                        if (deleteExpenditureList.isEmpty()) {
                            additionalViewModel.isDeleteModeLiveData.value = false
                        }
                    }

                    expenditureItem.isDeleteMode = !expenditureItem.isDeleteMode
                    section.notifyItemChanged(position)
                }else {
                    val checkCorrectExpenditureData = CheckCorrectExpenditureData()

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
                                            editCurrRest(
                                                newSumFloat = newSumFloat,
                                                oldSum = oldSum
                                            )
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

            groupieAdapter.setOnItemLongClickListener { item, view ->
                if (!additionalViewModel.getIsDeleteMode() && deleteExpenditureList.isEmpty()) {
                    additionalViewModel.isDeleteModeLiveData.value = true
                }

                val expenditureItem = item as ExpenditureItem
                val position = section.getPosition(expenditureItem)
                deleteExpenditureList.add(expenditureItem.expenditure)
                expenditureItem.isDeleteMode = !expenditureItem.isDeleteMode
                section.notifyItemChanged(position)

                true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu?.findItem(R.id.action_delete)?.isVisible = additionalViewModel.getIsDeleteMode()
        menu?.findItem(R.id.action_select_all)?.isVisible = additionalViewModel.getIsDeleteMode()

        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete -> {
                val dialog = createConfirmAlertDialog(
                    title = getString(R.string.dialog_delete_expenses_title),
                    message = getString(R.string.dialog_delete_expenses_message),
                    btnPositiveText = getString(R.string.dialog_btn_delete)
                )
                dialog.setOnShowListener { dialogInterface ->
                    val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btnPositive.setOnClickListener {
                        expenditureViewModel.deleteExpenses(
                            expenses = deleteExpenditureList,
                            onSuccess = {
                                showShortToast(text = getString(R.string.toast_successful_delete))
                                deleteExpenditureList.clear()
                                additionalViewModel.isDeleteModeLiveData.value = false
                            },
                            onError = {
                                showShortToast(text = getString(R.string.toast_error_delete))
                            }
                        )
                        dialogInterface.dismiss()
                    }
                }
                dialog.show()
            }
            R.id.action_select_all -> {
                for (i in 0 until section.itemCount) {
                    val expenditureItem = section.getItem(i) as ExpenditureItem
                    if (!expenditureItem.isDeleteMode) {
                        val expenditure = expenditureItem.expenditure
                        deleteExpenditureList.add(expenditure)
                        expenditureItem.isDeleteMode = !expenditureItem.isDeleteMode
                        section.notifyItemChanged(i)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
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

    private fun hideRecyclerHeader() {
        with(binding) {
            if (expensesHeader.textViewDate.visibility == View.VISIBLE) {
                expensesHeader.textViewDate.visibility = View.GONE
                expensesHeader.textViewSum.visibility = View.GONE
                expensesHeader.textViewDescription.visibility = View.GONE
            }
        }
    }

    private fun showRecyclerHeader() {
        with(binding) {
            if (expensesHeader.textViewDate.visibility == View.GONE) {
                expensesHeader.textViewDate.visibility = View.VISIBLE
                expensesHeader.textViewSum.visibility = View.VISIBLE
                expensesHeader.textViewDescription.visibility = View.VISIBLE
            }
        }
    }

    inner class ExpenditureItem(
        val expenditure: Expenditure,
        var isDeleteMode: Boolean = false
    ): BindableItem<ItemExpenditureBinding>() {
        override fun bind(viewBinding: ItemExpenditureBinding, position: Int) {
            with(viewBinding) {
                val color = if (isDeleteMode) {
                    ContextCompat.getColor(this@MainActivity, R.color.delete_mode_in)
                }else {
                    ContextCompat.getColor(this@MainActivity, R.color.background_layout)
                }

                itemExpendireLayout.setBackgroundColor(color)
                textViewSum.text = expenditure.sum.toString()
                textViewDate.text = expenditure.date
                textViewDescription.text = expenditure.description
            }
        }

        override fun getLayout() = R.layout.item_expenditure
        override fun initializeViewBinding(view: View) = ItemExpenditureBinding.bind(view)
    }
}