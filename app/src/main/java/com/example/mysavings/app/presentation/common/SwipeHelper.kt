package com.example.mysavings.app.presentation.common

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mysavings.app.presentation.common.swipe.SwipeHelper
import java.util.*
import kotlin.math.abs
import kotlin.math.max

private fun List<SwipeHelper.UnderlayButton>.intrinsicWidth(): Float {
    if (isEmpty()) return 0.0f
    return map { it.intrinsicWidth }.reduce { acc, fl -> acc + fl }
}