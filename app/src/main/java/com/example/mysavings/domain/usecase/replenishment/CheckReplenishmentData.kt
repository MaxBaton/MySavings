package com.example.mysavings.domain.usecase.replenishment

import com.example.mysavings.domain.models.other.FailReplenishment
import com.example.mysavings.domain.models.other.FailReplenishmentMessageId

class CheckReplenishmentData(private val sumStr: String) {
    fun check(): FailReplenishment {
        if (sumStr.isBlank()) {
            return FailReplenishment.EmptySum(stringMessageId = FailReplenishmentMessageId.BLANK_SUM.stringId)
        }

        try {
            sumStr.toFloat()
        }catch (e: NumberFormatException) {
            return FailReplenishment.IncorrectSum(stringMessageId = FailReplenishmentMessageId.INCORRECT_SUM.stringId)
        }

        return FailReplenishment.AllOk
    }
}