package com.example.mysavings.app.presentation.common.dialogs

import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mysavings.R
import com.example.mysavings.app.presentation.common.toggleKeyboard
import com.example.mysavings.app.presentation.viewModel.RestViewModel
import com.example.mysavings.data.data.EnumRestDialogMode
import com.example.mysavings.data.models.other.RestDialogMode
import com.example.mysavings.databinding.DialogAddEdRestBinding


fun AppCompatActivity.createAddEdRestDialog(mode: RestDialogMode): Pair<AlertDialog, DialogAddEdRestBinding> {
    val binding = DialogAddEdRestBinding.inflate(LayoutInflater.from(this))

    with(binding) {
        val restViewModel: RestViewModel by this@createAddEdRestDialog.viewModels()
        val currRestUnit = restViewModel.restUnitLiveData.value

        if (mode == EnumRestDialogMode.EDIT.restDialogMode) {
            // Редактирование
            val restFloat = restViewModel.restLiveData.value?.rest
            val currRestStr = restFloat.toString()

            etAddRest.setText(currRestStr, TextView.BufferType.EDITABLE)
        }

        txtInputLayoutAddRest.hint = "${txtInputLayoutAddRest.hint}, $currRestUnit"
        etAddRest.requestFocus()
    }

    val (title, btnPositiveStr) = if (mode == EnumRestDialogMode.ADD.restDialogMode) {
        getString(R.string.dialog_add_add_rest_title) to getString(R.string.dialog_btn_add)
    }else {
        getString(R.string.dialog_add_ed_rest_title) to getString(R.string.dialog_btn_change)
    }

    val dialog = AlertDialog.Builder(this).apply {
        setCancelable(false)
        setView(binding.root)
        setTitle(title)
        setPositiveButton(btnPositiveStr, null)
        setNegativeButton(getString(R.string.dialog_btn_cancel)) { dialogInterface, _ ->
            this@createAddEdRestDialog.toggleKeyboard()
            dialogInterface.dismiss()
        }
    }.create()

    return dialog to binding
}