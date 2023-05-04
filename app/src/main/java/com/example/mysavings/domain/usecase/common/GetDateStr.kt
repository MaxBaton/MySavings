package com.example.mysavings.domain.usecase.common

import android.os.Build
import com.example.mysavings.data.data.DefaultValues
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class GetDateStr(
    private val day: Int,
    private val month: Int,
    private val year: Int
) {
    fun get(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.of(year, month, day)
            (date as LocalDate).format(DateTimeFormatter.ofPattern(DefaultValues.DATE_PATTERN_STRING))
        } else {
            val date = Date(year, month, day)
            SimpleDateFormat(DefaultValues.DATE_PATTERN_STRING).format(date)
        }
    }
}