package com.example.mysavings.app.presentation.common.dialogs

import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysavings.R
import com.example.mysavings.app.presentation.common.hideKeyboard
import com.example.mysavings.app.presentation.common.showShortToast
import com.example.mysavings.app.presentation.viewModel.RestViewModel
import com.example.mysavings.data.data.EnumAddMode
import com.example.mysavings.data.data.EnumRestDialogMode
import com.example.mysavings.databinding.DialogAddModeBinding
import com.example.mysavings.databinding.ItemAddModeBinding
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.viewbinding.BindableItem

fun AppCompatActivity.createAddModeDialog(): AlertDialog {
    val binding = DialogAddModeBinding.inflate(LayoutInflater.from(this))
    val groupieAdapter = GroupieAdapter()
    val section = Section()

    val restViewModel: RestViewModel by viewModels()

    EnumAddMode.values().forEach {
        section.add(AddModeItem(
            activity = this,
            enumAddMode = it
        ))
    }

    groupieAdapter.add(section)

    binding.recyclerViewAddMode.apply {
        layoutManager = LinearLayoutManager(this@createAddModeDialog, LinearLayoutManager.VERTICAL, false)
        addItemDecoration(DividerItemDecoration(this@createAddModeDialog, DividerItemDecoration.VERTICAL))
        adapter = groupieAdapter
    }

    groupieAdapter.setOnItemClickListener { item, view ->
        val addModeItem = item as AddModeItem
        when(addModeItem.enumAddMode) {
            EnumAddMode.REST -> {
                val (dialog, dialogBinding) = this.createAddEdRestDialog(mode = EnumRestDialogMode.ADD.restDialogMode)
                dialog.setOnShowListener { dialogInterface ->
                    val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btnPositive.setOnClickListener {
                        val restStr = dialogBinding.etAddRest.text.toString()
                        if (restStr.isBlank()) {
                            this.showShortToast(text = getString(R.string.toast_blank_rest))
                            return@setOnClickListener
                        }else {
                            val restFloat = restStr.trim().toFloat()
                            restViewModel.addAdditionalRest(additionalRest = restFloat)
                            dialogInterface.dismiss()
                        }
                    }
                }
                dialog.show()
            }
            EnumAddMode.EXPENSES -> this.showShortToast(text = "expenses")
            EnumAddMode.ACCUMULATIONS -> this.showShortToast(text = "accumualtion")
        }
    }

    val dialog = AlertDialog.Builder(this).apply {
        setCancelable(false)
        setTitle(this.context.getString(R.string.dialog_add_modes_title))
        setView(binding.root)
        setPositiveButton("ok") { dialogInterface, _ -> dialogInterface.dismiss() }
    }.create()

    return dialog
}

class AddModeItem(
    private val activity: AppCompatActivity,
    val enumAddMode: EnumAddMode
): BindableItem<ItemAddModeBinding>() {
    override fun bind(viewBinding: ItemAddModeBinding, position: Int) {
        with(viewBinding) {
            textViewModeName.text = activity.getString(enumAddMode.addMode.nameStringId)
        }
    }

    override fun getLayout() = R.layout.item_add_mode

    override fun initializeViewBinding(view: View) = ItemAddModeBinding.bind(view)

}