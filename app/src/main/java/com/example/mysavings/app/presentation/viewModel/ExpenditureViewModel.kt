package com.example.mysavings.app.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysavings.data.data.DefaultValues
import com.example.mysavings.domain.models.repository.Expenditure
import com.example.mysavings.domain.repository.ExpenditureRepository
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

    init {
        getExpenses()
    }

    private fun getExpenses() {
        scopeIO.launch {
            val expenses = expenditureRepository.getExpenses()
            if (expenses != null) {
                viewModelScope.launch {
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

            val isAddExpenditure = expenditureRepository.add(expenditure = expenditure)
            viewModelScope.launch {
                if (isAddExpenditure) {
                    mutableExpensesLiveData.value?.add(expenditure)
                    mutableExpensesLiveData.value = mutableExpensesLiveData.value
                    onSuccess()
                }else {
                    onError()
                }
            }
        }
    }

    fun edExpenditure(expenditure: Expenditure, oldSum: Float, onSuccess: () -> Unit, onError: () -> Unit) {
        scopeIO.launch {
            val isEditExpenditure = expenditureRepository.update(expenditure = expenditure)
            viewModelScope.launch {
                if (isEditExpenditure) {
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
                expenses.forEach { expenditure ->
                    val isDelete = expenditureRepository.delete(expenditure = expenditure)
                    if (!isDelete && isDeleteAll) {
                        isDeleteAll = false
                    }else if (isDelete) {
                        mutableExpensesLiveData.value?.remove(expenditure)
                    }
                }
                viewModelScope.launch {
                    if (isDeleteAll) {
                        mutableExpensesLiveData.value = mutableExpensesLiveData.value
                        onSuccess()
                    }else {
                        onError()
                    }
                }
            }
        }
    }
}