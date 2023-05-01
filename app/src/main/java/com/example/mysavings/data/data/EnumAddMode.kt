package com.example.mysavings.data.data

import com.example.mysavings.R
import com.example.mysavings.data.models.other.AddMode

enum class EnumAddMode(val addMode: AddMode) {
    REST(AddMode(nameStringId = R.string.enum_add_rest_name)),
    EXPENSES(AddMode(nameStringId = R.string.enum_add_expenses_name)),
    ACCUMULATIONS(AddMode(nameStringId = R.string.enum_add_accumulations_name))
}