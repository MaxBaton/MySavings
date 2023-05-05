package com.example.mysavings.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysavings.domain.models.repository.Accumulation
import com.example.mysavings.domain.repository.AccumulationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccumulationsViewModel @Inject constructor(
    private val accumulationRepository: AccumulationRepository
): ViewModel() {
    private val scopIO = CoroutineScope(Dispatchers.IO)
    // Accumulations
    private val mutableAccumulationsLiveData = MutableLiveData<MutableList<Accumulation>>()
    val accumulationsLiveData: LiveData<MutableList<Accumulation>> = mutableAccumulationsLiveData

    init {
        getAllAccumulations()
    }

    private fun getAllAccumulations() {
        scopIO.launch {
            val accumulations = accumulationRepository.getAccumulation()
            if (accumulations != null) {
                viewModelScope.launch {
                    accumulations?.let { mutableAccumulationsLiveData.value = it }
                }
            }
        }
    }

    fun addAccumulation(name: String, sum: Float, onSuccess: () -> Unit, onError: () -> Unit) {
        scopIO.launch {
            val accumulation = Accumulation(
                name = name,
                sum = sum
            )
            val isAdd = accumulationRepository.add(accumulation = accumulation)
            viewModelScope.launch {
                if (isAdd) {
                    mutableAccumulationsLiveData.value?.let {
                        it.add(accumulation)
                        mutableAccumulationsLiveData.value = it
                    }
                    onSuccess()
                }else {
                    onError()
                }
            }
        }
    }

    fun addReplenishmentForAccumulation(sumAdd: Float, accumulation: Accumulation, onSuccess: () -> Unit, onError: () -> Unit) {
        scopIO.launch {
            accumulation.sum += sumAdd
            val isUpdate = accumulationRepository.update(accumulation = accumulation)
            viewModelScope.launch {
                if (isUpdate) {
                    mutableAccumulationsLiveData.value = mutableAccumulationsLiveData.value
                    onSuccess()
                }else {
                    onError()
                }
            }
        }
    }

    fun deleteAccumulation(accumulation: Accumulation, onSuccess: () -> Unit, onError: () -> Unit) {
        scopIO.launch {
            val isDelete = accumulationRepository.delete(accumulation = accumulation)
            viewModelScope.launch {
                if (isDelete) {
                    mutableAccumulationsLiveData.value?.remove(accumulation)
                    mutableAccumulationsLiveData.value = mutableAccumulationsLiveData.value
                    onSuccess()
                }else {
                    onError()
                }
            }
        }
    }

    fun editAccumulation(id: Int, sumFloat: Float, name: String, onSuccess: () -> Unit, onError: () -> Unit) {
        scopIO.launch {
            val accumulation = Accumulation(
                id = id,
                name = name,
                sum = sumFloat
            )
            val isEdit = accumulationRepository.update(accumulation = accumulation)
            viewModelScope.launch {
                if (isEdit) {
                    val oldAccumulation = mutableAccumulationsLiveData.value?.find { it.id == id }
                    oldAccumulation?.let {
                        it.sum = sumFloat
                        it.name = name
                    }
                    mutableAccumulationsLiveData.value = mutableAccumulationsLiveData.value
                    onSuccess()
                }else {
                    onError()
                }
            }
        }
    }
}