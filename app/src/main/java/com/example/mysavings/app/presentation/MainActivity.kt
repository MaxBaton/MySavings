package com.example.mysavings.app.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mysavings.R
import com.example.mysavings.app.presentation.common.dialogs.createAddEdRestDialog
import com.example.mysavings.app.presentation.common.dialogs.createAddModeDialog
import com.example.mysavings.app.presentation.common.showShortToast
import com.example.mysavings.app.presentation.common.toggleKeyboard
import com.example.mysavings.app.presentation.viewModel.ExpenditureViewModel
import com.example.mysavings.app.presentation.viewModel.RestViewModel
import com.example.mysavings.data.data.EnumRestDialogMode
import com.example.mysavings.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val restViewModel: RestViewModel by viewModels()
    private val expenditureViewModel: ExpenditureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            restViewModel.restStrLiveData.observe(this@MainActivity) { restStr ->
                textViewRest.text = restStr
            }

            expenditureViewModel.expensesLiveData.observe(this@MainActivity) { expenses ->
                expenses?.forEach {

                }
            }

            fabAdd.setOnClickListener {
                val dialog = this@MainActivity.createAddModeDialog()
                dialog.show()
            }

            fabEd.setOnClickListener {
                val (dialog, dialogBinding) = this@MainActivity.createAddEdRestDialog(mode = EnumRestDialogMode.EDIT.restDialogMode)
                dialog.setOnShowListener { dialogInterface ->
                    this@MainActivity.toggleKeyboard()

                    val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    btnPositive.setOnClickListener {
                        val restStr = dialogBinding.etAddRest.text.toString().trim()
                        if (restStr == "") {
                            this@MainActivity.showShortToast(text = getString(R.string.toast_blank_rest))
                            return@setOnClickListener
                        }else {
                            val restFloat = restStr.toFloat()
                            restViewModel.changeRest(newRest = restFloat)
                            dialogInterface.dismiss()
                        }
                    }
                }
                dialog.show()
            }
        }
    }
}