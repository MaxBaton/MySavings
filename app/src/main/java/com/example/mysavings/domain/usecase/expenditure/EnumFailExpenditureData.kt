package com.example.mysavings.domain.usecase.expenditure

import com.example.mysavings.R
import com.example.mysavings.domain.models.other.FailExpenditureData

enum class EnumFailExpenditureData(val failExpenditureData: FailExpenditureData) {
    INCORRECT_SUM(FailExpenditureData(stringMessageId = R.string.toast_blank_sum)),
    TOO_BIG_EXPENDITURE(FailExpenditureData(stringMessageId = R.string.toast_too_big_expenditure)),
    ALL_OK(FailExpenditureData(stringMessageId = 0))
}