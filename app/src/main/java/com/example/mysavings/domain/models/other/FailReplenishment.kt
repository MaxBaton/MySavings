package com.example.mysavings.domain.models.other

sealed class FailReplenishment{
    class EmptySum(val stringMessageId: Int): FailReplenishment()
    class IncorrectSum(val stringMessageId: Int): FailReplenishment()
    object AllOk: FailReplenishment()
}
