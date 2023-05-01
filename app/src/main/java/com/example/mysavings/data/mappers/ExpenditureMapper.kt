package com.example.mysavings.data.mappers

import com.example.mysavings.data.models.db.ExpenditureData
import com.example.mysavings.domain.models.Expenditure

fun Expenditure.mapToExpenditureData(): ExpenditureData {
    return ExpenditureData(
        id = this.id,
        date = this.date,
        sum = this.sum,
        text = this.text
    )
}

fun List<Expenditure>.mapToListExpenditureData(): List<ExpenditureData> {
    val list = mutableListOf<ExpenditureData>()

    this.forEach { expenditure ->
        val expenditureData = expenditure.mapToExpenditureData()
        list.add(expenditureData)
    }

    return list
}

fun ExpenditureData.mapToExpenditure(): Expenditure {
    return Expenditure(
        id = this.id,
        date = this.date,
        sum = this.sum,
        text = this.text
    )
}

fun List<ExpenditureData>.mapToListExpenditure(): List<Expenditure> {
    val list = mutableListOf<Expenditure>()

    this.forEach { expenditureData ->
        val expenditure = expenditureData.mapToExpenditure()
        list.add(expenditure)
    }

    return list
}