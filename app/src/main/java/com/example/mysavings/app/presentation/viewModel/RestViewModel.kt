package com.example.mysavings.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysavings.data.data.DefaultValues
import com.example.mysavings.domain.models.repository.Rest
import com.example.mysavings.domain.repository.RestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class RestViewModel @Inject constructor(
    private val restRepository: RestRepository
): ViewModel() {
    // Scopes
    private val scopeIO = CoroutineScope(Dispatchers.IO)
    // Rest String
    private val mutableRestStrLiveData = MutableLiveData<String>()
    val restStrLiveData: LiveData<String> = mutableRestStrLiveData
    // Rest object
    private val mutableRestLiveData = MutableLiveData<Rest?>()
    val restLiveData: LiveData<Rest?> = mutableRestLiveData
    // Unit
    private val mutableUnitLiveData = MutableLiveData<String>()
    val restUnitLiveData: LiveData<String> = mutableUnitLiveData

    init {
        getRestStr()
    }

    private fun getRestStr() {
        scopeIO.launch {
            val restAsync = async { restRepository.getRest() }
            val unitAsync = async { getRestUnit() }

            // Если остатка нет - берем нулевой и записываем его в БД
            val rest = if (restAsync.await() == null) {
                val initialRest = Rest(id = 1, rest = DefaultValues.DEFAULT_REST)
                restRepository.add(rest = initialRest)
                initialRest
            }else {
                restAsync.await()
            }

            viewModelScope.launch {
                val unit = unitAsync.await()

                mutableRestLiveData.value = rest
                mutableUnitLiveData.value = unit

                mutableRestStrLiveData.value = getFormatRestStr(
                    rest = rest?.rest ?: DefaultValues.DEFAULT_REST,
                    unit = unit
                )
            }
        }
    }

    private fun getRestUnit() = "BY"

    private fun getFormatRestStr(rest: Float, unit: String) = "${DecimalFormat("##.##").format(rest)} $unit"

    fun getCurrRest(): Float {
        return restLiveData.value?.rest ?: DefaultValues.DEFAULT_REST
    }

    fun changeRest(newRest: Float) {
        val _rest = mutableRestLiveData.value
        _rest?.let { rest ->
            scopeIO.launch {
                rest.rest = newRest
                val isUpdate = restRepository.update(rest = rest)
                viewModelScope.launch {
                    if (isUpdate) {
                        mutableRestLiveData.value = rest
                        mutableRestStrLiveData.value = getFormatRestStr(
                            rest = rest.rest,
                            unit = mutableUnitLiveData.value ?: ""
                        )
                    }
                }
            }
        }
    }

    fun addAdditionalRest(additionalRest: Float, isAdd: Boolean = true) {
        val _rest = mutableRestLiveData.value
        _rest?.let { rest ->
            scopeIO.launch {
                if (isAdd) {
                    rest.rest += additionalRest
                }else {
                    rest.rest -= additionalRest
                }
                val isUpdate = restRepository.update(rest = rest)
                viewModelScope.launch {
                    if (isUpdate) {
                        mutableRestLiveData.value = rest
                        mutableRestStrLiveData.value = getFormatRestStr(
                            rest = rest.rest,
                            unit = mutableUnitLiveData.value ?: ""
                        )
                    }
                }
            }
        }
    }
}