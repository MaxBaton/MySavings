package com.example.mysavings.app.presentation.common.dialogs

import android.os.Build
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mysavings.R
import com.example.mysavings.data.data.DefaultValues
import com.example.mysavings.databinding.DialogAddEdExpenditureBinding
import com.example.mysavings.databinding.DialogDatePickerBinding
import com.example.mysavings.domain.models.repository.Expenditure
import com.example.mysavings.domain.usecase.common.GetCurrentDate
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

fun AppCompatActivity.createAddEdExpenditureDialog(expenditure: Expenditure? = null): Pair<AlertDialog, DialogAddEdExpenditureBinding> {
    val binding = DialogAddEdExpenditureBinding.inflate(LayoutInflater.from(this))

    with(binding) {
        val currDateStr = expenditure?.date ?: GetCurrentDate().getStr()
        etAddMoneyExpensesDate.setText(currDateStr, TextView.BufferType.EDITABLE)
        expenditure?.let {
            etAddMoneyExpensesSum.setText(it.sum.toString(), TextView.BufferType.EDITABLE)
            etAddMoneyExpensesDescription.setText(it.description, TextView.BufferType.EDITABLE)
        }

        etAddMoneyExpensesDate.setOnClickListener {
            val (dialogDate, dialogDateBinding) = createDatePickerDialog()
            dialogDate.setOnShowListener {
                val btnPositive = dialogDate.getButton(AlertDialog.BUTTON_POSITIVE)
                btnPositive.setOnClickListener {
                    val day = dialogDateBinding.datePicker.dayOfMonth
                    val month = dialogDateBinding.datePicker.month + 1
                    val year = dialogDateBinding.datePicker.year
                    val dateStr = getDateStr(
                        day = day,
                        month = month,
                        year = year
                    )
                    this.etAddMoneyExpensesDate.setText(dateStr, TextView.BufferType.EDITABLE)
                    dialogDate.dismiss()
                }
            }
            dialogDate.show()
        }
    }

    val dialog = AlertDialog.Builder(this).apply {
        setCancelable(false)
        setView(binding.root)
        val (title, btnPositiveTitle) = if (expenditure == null) {
            getString(R.string.dialog_add_expenditure_title) to getString(R.string.dialog_btn_add)
        } else {
            getString(R.string.dialog_ed_expenditure_title) to getString(R.string.dialog_btn_change)
        }
        setTitle(title)
        setPositiveButton(btnPositiveTitle, null)
        setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
    }.create()

    return dialog to binding
}


fun AppCompatActivity.createDatePickerDialog(): Pair<AlertDialog, DialogDatePickerBinding> {
    val binding = DialogDatePickerBinding.inflate(LayoutInflater.from(this))

    val dialog = AlertDialog.Builder(this).apply {
        setCancelable(false)
        setView(binding.root)
        setTitle(getString(R.string.dialog_date_picker_title))
        setPositiveButton(getString(R.string.dialog_btn_ok), null)
        setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
    }.create()

    return dialog to binding
}

private fun getCurrentDateStr(): String {
    val date = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat(DefaultValues.DATE_PATTERN_STRING)
    return dateFormat.format(date)
}

private fun getDateStr(day: Int, month: Int, year: Int): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val date = LocalDate.of(year, month, day)
        (date as LocalDate).format(DateTimeFormatter.ofPattern(DefaultValues.DATE_PATTERN_STRING))
    } else {
        val date = Date(year, month, day)
        SimpleDateFormat(DefaultValues.DATE_PATTERN_STRING).format(date)
    }
}