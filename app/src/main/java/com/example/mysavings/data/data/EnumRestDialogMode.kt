package com.example.mysavings.data.data

import com.example.mysavings.R
import com.example.mysavings.data.models.other.RestDialogMode

enum class EnumRestDialogMode(val restDialogMode: RestDialogMode) {
    ADD(RestDialogMode(nameStringId = R.string.dialog_add_add_rest_title)),
    EDIT(RestDialogMode(nameStringId = R.string.dialog_add_ed_rest_title))
}