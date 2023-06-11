package com.example.mysavings.domain.usecase.expenditure

import com.example.mysavings.domain.models.repository.Expenditure

sealed class ExpensesOperations {
    object GetAll: ExpensesOperations()
    class Add(val expenditure: Expenditure): ExpensesOperations()
    class Edit(val position: Int): ExpensesOperations()
    class Delete(val deleteExpenses: List<Expenditure>): ExpensesOperations()
}
