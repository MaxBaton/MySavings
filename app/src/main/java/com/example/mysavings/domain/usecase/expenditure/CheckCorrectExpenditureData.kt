package com.example.mysavings.domain.usecase.expenditure

class CheckCorrectExpenditureData {
    fun check(sumStr: String, currRest: Float): EnumFailExpenditureData {
        if (sumStr.isEmpty()) {
            return EnumFailExpenditureData.INCORRECT_SUM
        }

        val sumFloat = sumStr.toFloat()
        val newRest = currRest - sumFloat
        if (newRest < 0) {
            return EnumFailExpenditureData.TOO_BIG_EXPENDITURE
        }

        return EnumFailExpenditureData.ALL_OK
    }

    fun checkEditable(newSumStr: String, oldSum: Float, currRest: Float): EnumFailExpenditureData {
        if (newSumStr.isEmpty()) {
            return EnumFailExpenditureData.INCORRECT_SUM
        }

        val newSumFloat = newSumStr.toFloat()
        if (newSumFloat > oldSum) {
            val diff = newSumFloat - oldSum
            if ((currRest - diff) < 0) {
                return EnumFailExpenditureData.TOO_BIG_EXPENDITURE
            }
        }

        return EnumFailExpenditureData.ALL_OK
    }
}