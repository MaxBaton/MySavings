package com.example.mysavings.domain.usecase.expenditure

import com.example.mysavings.domain.models.other.FailExpenditure
import com.example.mysavings.domain.models.other.FailExpenditureMessageId

class CheckCorrectExpenditureData {

    fun check(sumStr: String, currRest: Float): FailExpenditure {
        if (sumStr.isEmpty()) {
            return FailExpenditure.IncorrectSum(stringMessageId = FailExpenditureMessageId.INCORRECT_SUM.stringId)
        }

        val sumFloat = sumStr.toFloat()
        val newRest = currRest - sumFloat
        if (newRest < 0) {
            return FailExpenditure.TooBigExpenditure(stringMessageId = FailExpenditureMessageId.TOO_BIG_EXPENDITURE.stringId)
        }

        return FailExpenditure.AllOk
    }

    fun checkEditable(newSumStr: String, oldSum: Float, currRest: Float): FailExpenditure {
        if (newSumStr.isEmpty()) {
            return FailExpenditure.IncorrectSum(stringMessageId = FailExpenditureMessageId.INCORRECT_SUM.stringId)
        }

        val newSumFloat = newSumStr.toFloat()
        if (newSumFloat > oldSum) {
            val diff = newSumFloat - oldSum
            if ((currRest - diff) < 0) {
                return FailExpenditure.TooBigExpenditure(stringMessageId = FailExpenditureMessageId.TOO_BIG_EXPENDITURE.stringId)
            }
        }

        return FailExpenditure.AllOk
    }
}