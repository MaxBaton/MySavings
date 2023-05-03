package com.example.mysavings.app.presentation.common.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.mysavings.R

fun Context.createConfirmAlertDialog(title: String, message: String, btnPositiveText: String): AlertDialog {
    return AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(btnPositiveText, null)
        setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialogInterace, _ ->
            dialogInterace.dismiss()
        }
    }.create()
}