package com.example.mysavings.app.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysavings.domain.models.Expenditure
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

    fun addExpenditure(sum: Float, description: String, date: String) {
        scopeIO.launch {
            val expenditure = Expenditure(
                date = date,
                sum = sum,
                description = description
            )

            val isAddExpenditure = expenditureRepository.add(expenditure = expenditure)
            if (isAddExpenditure) {
                viewModelScope.launch {
                    mutableExpensesLiveData.value?.add(expenditure)
                }
            }
        }
    }
}