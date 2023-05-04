package com.example.mysavings.app.presentation.common.dialogs

import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mysavings.R
import com.example.mysavings.databinding.DialogAddReplenishmentBinding
import com.example.mysavings.domain.usecase.common.GetCurrentDate
import com.example.mysavings.domain.usecase.common.GetDateStr

fun AppCompatActivity.createAddReplenishmentDialog(): Pair<AlertDialog, DialogAddReplenishmentBinding> {
    val binding = DialogAddReplenishmentBinding.inflate(LayoutInflater.from(this))

    with(binding) {
        val currDateStr = GetCurrentDate().getStr()
        etAddReplenishmentDate.setText(currDateStr, TextView.BufferType.EDITABLE)

        etAddReplenishmentDate.setOnClickListener {
            val (dialogDate, dialogDateBinding) = createDatePickerDialog()
            dialogDate.setOnShowListener {
                val btnPositive = dialogDate.getButton(AlertDialog.BUTTON_POSITIVE)
                btnPositive.setOnClickListener {
                    val day = dialogDateBinding.datePicker.dayOfMonth
                    val month = dialogDateBinding.datePicker.month + 1
                    val year = dialogDateBinding.datePicker.year
                    val dateStr = GetDateStr(
                        day = day,
                        month = month,
                        year = year
                    ).get()
                    this.etAddReplenishmentDate.setText(dateStr, TextView.BufferType.EDITABLE)
                    dialogDate.dismiss()
                }
            }
            dialogDate.show()
        }
    }

    val dialog = AlertDialog.Builder(this).apply {
        setCancelable(false)
        setView(binding.root)
        setTitle(getString(R.string.dialog_add_replenishment_title))
        setPositiveButton(getString(R.string.dialog_btn_add), null)
        setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
    }.create()

    return dialog to binding
}