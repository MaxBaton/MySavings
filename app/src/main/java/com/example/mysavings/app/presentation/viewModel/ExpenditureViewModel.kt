package com.example.mysavings.app.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysavings.domain.models.repository.Expenditure
import com.example.mysavings.domain.repository.ExpenditureRepository
import com.example.mysavings.domain.usecase.expenditure.ExpenditureOperations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenditureViewModel @Inject constructor(
    private val expenditureRepository: ExpenditureRepository
): ViewModel() {
    private val scopeIO = CoroutineScope(Dispatchers.IO)
    // List Expenditure
    private val mutableExpensesLiveData = MutableLiveData<MutableList<Expenditure>?>()
    val expensesLiveData = mutableExpensesLiveData
    // Current operation
    private val currOperationMutableLiveData = MutableLiveData<ExpenditureOperations>()

    init {
        getExpenses()
    }

    private fun getExpenses() {
        scopeIO.launch {
            val expenses = expenditureRepository.getExpenses()
            if (expenses != null) {
                viewModelScope.launch {
                    currOperationMutableLiveData.value = ExpenditureOperations.GetAll
                    mutableExpensesLiveData.value = expenses
                }
            }
        }
    }

    fun addExpenditure(sum: Float, description: String, date: String, onSuccess: () -> Unit, onError: () -> Unit) {
        scopeIO.launch {
            val expenditure = Expenditure(
                date = date,
                sum = sum,
                description = description
            )

            val idAddExpenditure = expenditureRepository.add(expenditure = expenditure)
            viewModelScope.launch {
                if (idAddExpenditure != -1) {
                    expenditure.id = idAddExpenditure
                    currOperationMutableLiveData.value = ExpenditureOperations.Add(expenditure = expenditure)
                    mutableExpensesLiveData.value?.add(expenditure)
                    mutableExpensesLiveData.value = mutableExpensesLiveData.value
                    onSuccess()
                }else {
                    onError()
                }
            }
        }
    }

    fun edExpenditure(expenditure: Expenditure, position: Int, oldSum: Float, onSuccess: () -> Unit, onError: () -> Unit) {
        scopeIO.launch {
            val isEditExpenditure = expenditureRepository.update(expenditure = expenditure)
            viewModelScope.launch {
                if (isEditExpenditure) {
                    currOperationMutableLiveData.value = ExpenditureOperations.Edit(position = position)
                    mutableExpensesLiveData.value = mutableExpensesLiveData.value
                    onSuccess()
                }else {
                    onError()
                }
            }
        }
    }

    fun deleteExpenses(expenses: List<Expenditure>, onSuccess: () -> Unit, onError: () -> Unit) {
        if (expenses.isNotEmpty()) {
            scopeIO.launch {
                var isDeleteAll = true
                expenses.forEachIndexed { index, expenditure ->
                    val isDelete = expenditureRepository.delete(expenditure = expenditure)
                    if (!isDelete && isDeleteAll) {
                        isDeleteAll = false
                    }else if (isDelete) {
                        mutableExpensesLiveData.value?.remove(expenditure)
                    }
                }
                viewModelScope.launch {
                    if (isDeleteAll) {
                        currOperationMutableLiveData.value = ExpenditureOperations.Delete(deleteExpenses = expenses)
                        mutableExpensesLiveData.value = mutableExpensesLiveData.value
                        onSuccess()
                    }else {
                        onError()
                    }
                }
            }
        }
    }

    private fun getDeletedIndexes(expenses: List<Expenditure>): List<Int> {
        val deleteIndexes = mutableListOf<Int>()
        mutableExpensesLiveData.value?.forEachIndexed { index, expenditure ->
            val indexOf = expenses.indexOf(expenditure)
            if (indexOf != -1) {
                deleteIndexes.add(element = index)
            }
        }
        return deleteIndexes
    }

    fun getCurrOperation(): ExpenditureOperations {
        return currOperationMutableLiveData.value ?: ExpenditureOperations.GetAll
    }
}