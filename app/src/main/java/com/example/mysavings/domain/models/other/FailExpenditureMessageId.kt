package com.example.mysavings.domain.models.other

import com.example.mysavings.R

enum class FailExpenditureMessageId(val stringId: Int) {
    INCORRECT_SUM(R.string.toast_blank_sum),
    TOO_BIG_EXPENDITURE(R.string.toast_too_big_expenditure),
}