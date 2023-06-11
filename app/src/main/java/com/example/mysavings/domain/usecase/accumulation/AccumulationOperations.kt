package com.example.mysavings.domain.usecase.accumulation

import com.example.mysavings.domain.models.repository.Accumulation

sealed class AccumulationOperations {
    object GetAll: AccumulationOperations()
    class Add(val accumulation: Accumulation): AccumulationOperations()
    class Edit(val position: Int): AccumulationOperations()
    class Delete(val position: Int): AccumulationOperations()
}
