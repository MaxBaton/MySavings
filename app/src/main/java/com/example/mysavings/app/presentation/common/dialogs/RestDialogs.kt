package com.example.mysavings.app.presentation.common.dialogs

import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mysavings.R
import com.example.mysavings.app.presentation.common.toggleKeyboard
import com.example.mysavings.app.presentation.viewModel.RestViewModel
import com.example.mysavings.databinding.DialogAddEdRestBinding


fun AppCompatActivity.createAddEdRestDialog(restViewModel: RestViewModel): Pair<AlertDialog, DialogAddEdRestBinding> {
    val binding = DialogAddEdRestBinding.inflate(LayoutInflater.from(this))

    val restFloat = restViewModel.restLiveData.value?.rest
    val currRestStr = restFloat.toString()
    val currRestUnit = restViewModel.restUnitLiveData.value

    with(binding) {
        etAddRest.setText(currRestStr, TextView.BufferType.EDITABLE)
        txtInputLayoutAddRest.hint = "${txtInputLayoutAddRest.hint}, $currRestUnit"
        etAddRest.requestFocus()
    }

    val dialog = AlertDialog.Builder(this).apply {
        setCancelable(false)
        setView(binding.root)
        setTitle(R.string.dialog_add_ed_rest_title)
        setPositiveButton(getString(R.string.dialog_btn_change), null)
        setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialogInterface, _ ->
            this@createAddEdRestDialog.toggleKeyboard()
            dialogInterface.dismiss()
        }
    }.create()

    return dialog to binding
}