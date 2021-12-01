package me.kovp.canvasapp.utils

import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Paint
import android.text.Editable

fun Editable?.toIntOrNull() = this.toString().toIntOrNull()

fun Editable?.toDoubleOrNull() = this.toString().toDoubleOrNull()

fun coloredPaint(color: Int): Paint = prettyPaint().also {
    it.color = color
}

fun textPaint(): Paint = prettyPaint().also {
    it.textSize = 32f
    it.textAlign = Paint.Align.CENTER
    it.color = Color.BLACK
}

fun coloredGradient(color: Int): Paint = prettyPaint().also {
    it.color = color
    it.maskFilter = BlurMaskFilter(60f, BlurMaskFilter.Blur.NORMAL)
    it.strokeWidth = 20f
    it.style = Paint.Style.STROKE
}

private fun prettyPaint(): Paint = Paint().also {
    it.isAntiAlias = true
}