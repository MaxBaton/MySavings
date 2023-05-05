package com.example.mysavings.app.presentation.common.swipe

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mysavings.R

class SwipeButtons(private val context: Context) {
    fun deleteButton(swipeButtonClickListener: SwipeButtonClickListener) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            context = context,
            title = context.getString(R.string.dialog_btn_delete),
            textSize = 14f,
            colorRes = R.color.color_swipe_button_1,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    swipeButtonClickListener.click()
                }
            },
            icon = R.drawable.delete_swipe_icon
        )
    }

    fun editButton(swipeButtonClickListener: SwipeButtonClickListener) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            context = context,
            title = context.getString(R.string.dialog_btn_edit),
            textSize = 14f,
            colorRes = R.color.color_swipe_button_2,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    swipeButtonClickListener.click()
                }
            },
            icon = R.drawable.edit_swipe_icon
        )
    }
}