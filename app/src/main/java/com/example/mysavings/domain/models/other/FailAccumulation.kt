package com.example.mysavings.domain.models.other

sealed class FailAccumulation{
    class EmptyName(val stringMessageId: Int): FailAccumulation()
    class EmptySum(val stringMessageId: Int): FailAccumulation()
    class IncorrectSum(val stringMessageId: Int): FailAccumulation()
    object AllOk: FailAccumulation()
}
