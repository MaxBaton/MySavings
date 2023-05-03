package com.example.mysavings.domain.models.other

sealed class FailExpenditure{
    class IncorrectSum(val stringMessageId: Int): FailExpenditure()
    class TooBigExpenditure(val stringMessageId: Int): FailExpenditure()
    object AllOk : FailExpenditure()
}
