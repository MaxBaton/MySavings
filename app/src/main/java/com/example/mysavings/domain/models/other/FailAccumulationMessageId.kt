package com.example.mysavings.domain.models.other

import com.example.mysavings.R

enum class FailAccumulationMessageId(val stringId: Int) {
    BLANK_SUM(R.string.toast_blank_sum),
    BLANK_NAME(R.string.toast_blank_name),
    INCORRECT_SUM(R.string.toast_incorrect_sum)
}