package com.example.mysavings.app.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    private val mutableRestStrLiveData = MutableLiveData<String>()
    val restStrLiveData: LiveData<String> = mutableRestStrLiveData

    init {
        getRestStr()
    }

    private fun getRestStr() {
        CoroutineScope(Dispatchers.IO).launch {
            val rest = async { restRepository.getRest() }
            val unit = async { getRestUnit() }

            CoroutineScope(Dispatchers.Main).launch {
                mutableRestStrLiveData.value = getFormatRestStr(
                    rest = rest.await(),
                    unit = unit.await()
                )
            }
        }
    }

    private fun getRestUnit() = "BY"

    private fun getFormatRestStr(rest: Float, unit: String) = "${DecimalFormat("##.##").format(rest)} $unit"
}