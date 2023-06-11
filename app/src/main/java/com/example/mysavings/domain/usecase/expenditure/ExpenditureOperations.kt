package com.example.mysavings.domain.usecase.expenditure

import com.example.mysavings.domain.models.repository.Expenditure

sealed class ExpenditureOperations {
    object GetAll: ExpenditureOperations()
    class Add(val expenditure: Expenditure): ExpenditureOperations()
    class Edit(val position: Int): ExpenditureOperations()
    class Delete(val deleteExpenses: List<Expenditure>): ExpenditureOperations()
}
