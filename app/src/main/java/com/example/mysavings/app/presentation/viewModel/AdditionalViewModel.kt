package com.example.mysavings.app.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysavings.data.data.DefaultValues

class AdditionalViewModel: ViewModel() {
    val isDeleteModeLiveData = MutableLiveData<Boolean>()

    init {
        isDeleteModeLiveData.value = DefaultValues.DEFAULT_IS_DELETE_MODE
    }

    fun getIsDeleteMode(): Boolean {
        return isDeleteModeLiveData.value ?: DefaultValues.DEFAULT_IS_DELETE_MODE
    }
}