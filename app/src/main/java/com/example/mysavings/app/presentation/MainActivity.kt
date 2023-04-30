package com.example.mysavings.app.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.mysavings.R
import com.example.mysavings.app.presentation.common.createModeDialogWithRecyclerView
import com.example.mysavings.app.presentation.common.showShortToast
import com.example.mysavings.app.presentation.viewModel.RestViewModel
import com.example.mysavings.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val restViewModel: RestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            restViewModel.restStrLiveData.observe(this@MainActivity) { restStr ->
                textViewRest.text = restStr
            }

            fabAdd.setOnClickListener {
                val dialog = this@MainActivity.createModeDialogWithRecyclerView()
                dialog.show()
            }

            fabEd.setOnClickListener {
                this@MainActivity.showShortToast(text = "ed")
            }
        }
    }
}