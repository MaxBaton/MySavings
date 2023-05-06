package com.example.mysavings.domain.usecase.main

import androidx.appcompat.app.AlertDialog
import java.util.*

class QueueDialogs(private val allShowingDialogs: Queue<AlertDialog>) {
    fun add(dialog: AlertDialog) {
        allShowingDialogs.add(dialog)
        dialog.setOnDismissListener {
            allShowingDialogs.remove(dialog)
        }
    }
}