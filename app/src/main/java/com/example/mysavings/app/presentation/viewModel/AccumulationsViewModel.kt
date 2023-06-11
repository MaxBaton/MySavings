package com.example.mysavings.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysavings.domain.models.repository.Accumulation
import com.example.mysavings.domain.repository.AccumulationRepository
import com.example.mysavings.domain.usecase.accumulation.AccumulationOperations
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
    // Current operation
    private val currOperationMutableLiveData = MutableLiveData<AccumulationOperations>()

    init {
        getAllAccumulations()
    }

    private fun getAllAccumulations() {
        scopIO.launch {
            val accumulations = accumulationRepository.getAccumulation()
            if (accumulations != null) {
                viewModelScope.launch {
                    currOperationMutableLiveData.value = AccumulationOperations.GetAll
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
            val idAdd = accumulationRepository.add(accumulation = accumulation)
            viewModelScope.launch {
                if (idAdd != -1) {
                    accumulation.id = idAdd
                    currOperationMutableLiveData.value = AccumulationOperations.Add(accumulation = accumulation)
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

    fun deleteAccumulation(accumulation: Accumulation, position: Int, onSuccess: () -> Unit, onError: () -> Unit) {
        scopIO.launch {
            val isDelete = accumulationRepository.delete(accumulation = accumulation)
            viewModelScope.launch {
                if (isDelete) {
                    currOperationMutableLiveData.value = AccumulationOperations.Delete(position = position)
                    mutableAccumulationsLiveData.value?.remove(accumulation)
                    mutableAccumulationsLiveData.value = mutableAccumulationsLiveData.value
                    onSuccess()
                }else {
                    onError()
                }
            }
        }
    }

    fun editAccumulation(id: Int, sumFloat: Float, name: String, position: Int, onSuccess: () -> Unit, onError: () -> Unit) {
        scopIO.launch {
            val accumulation = Accumulation(
                id = id,
                name = name,
                sum = sumFloat
            )
            val isEdit = accumulationRepository.update(accumulation = accumulation)
            viewModelScope.launch {
                if (isEdit) {
                    currOperationMutableLiveData.value = AccumulationOperations.Edit(position = position)
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

    fun getCurrOperation(): AccumulationOperations {
        return currOperationMutableLiveData.value ?: AccumulationOperations.GetAll
    }

    fun setCurrOperations(operation: AccumulationOperations) {
        currOperationMutableLiveData.value = operation
    }
}