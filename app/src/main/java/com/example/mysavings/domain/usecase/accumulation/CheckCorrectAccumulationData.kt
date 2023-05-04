package com.example.mysavings.domain.usecase.accumulation

import com.example.mysavings.domain.models.other.FailAccumulation
import com.example.mysavings.domain.models.other.FailAccumulationMessageId

class CheckCorrectAccumulationData {
    fun check(name: String, sumStr: String): FailAccumulation {
        if (sumStr.isBlank()) {
            return FailAccumulation.EmptySum(FailAccumulationMessageId.BLANK_SUM.stringId)
        }

        if (name.isBlank()) {
            return FailAccumulation.EmptyName(FailAccumulationMessageId.BLANK_NAME.stringId)
        }

        try {
            sumStr.toFloat()
        }catch (e: java.lang.NumberFormatException) {
            return FailAccumulation.IncorrectSum(FailAccumulationMessageId.INCORRECT_SUM.stringId)
        }

        return FailAccumulation.AllOk
    }
}