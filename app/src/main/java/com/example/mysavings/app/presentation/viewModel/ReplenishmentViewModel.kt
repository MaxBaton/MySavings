package com.example.mysavings.app.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysavings.domain.models.repository.Replenishment
import com.example.mysavings.domain.repository.ReplenishmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplenishmentViewModel @Inject constructor(
    private val replenishmentRepository: ReplenishmentRepository
): ViewModel() {
    private val scopeIO = CoroutineScope(Dispatchers.IO)

    fun addReplenishment(sum: Float, date: String, idAccumulation: Int, onSuccess: () -> Unit, onError: () -> Unit) {
        scopeIO.launch {
            val replenishment = Replenishment(
                sum = sum,
                date = date,
                idAccumulation = idAccumulation
            )
            val isAdd = replenishmentRepository.add(replenishment = replenishment)
            viewModelScope.launch {
                if (isAdd) {
                    onSuccess()
                }else {
                    onError()
                }
            }
        }
    }
}