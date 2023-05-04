package com.example.mysavings.domain.usecase.common

import com.example.mysavings.data.data.DefaultValues
import java.text.SimpleDateFormat
import java.util.*

class GetCurrentDate {
    fun getStr(): String {
        val date = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat(DefaultValues.DATE_PATTERN_STRING)
        return dateFormat.format(date)
    }
}