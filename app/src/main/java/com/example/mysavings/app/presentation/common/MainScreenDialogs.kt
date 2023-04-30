package com.example.mysavings.app.presentation.common

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mysavings.R

fun AppCompatActivity.createModeDialogWithRecyclerView(): AlertDialog {
    val dialog = AlertDialog.Builder(this).apply {
        setCancelable(false)
        setTitle(this.context.getString(R.string.dialog_add_modes_title))
        setPositiveButton("ok") { dialogInterface, _ -> dialogInterface.dismiss() }
    }.create()

    return dialog
}