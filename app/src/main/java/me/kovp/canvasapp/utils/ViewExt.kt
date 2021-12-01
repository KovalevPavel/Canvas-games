package me.kovp.canvasapp.utils

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RadialGradient
import android.graphics.Shader.TileMode.CLAMP
import android.text.Editable

fun Editable?.toIntOrNull() = this.toString().toIntOrNull()

fun Editable?.toDoubleOrNull() = this.toString().toDoubleOrNull()

fun coloredPaint(color: Int): Paint = prettyPaint().also {
    it.color = color
    it.isAntiAlias = true
}

fun textPaint(): Paint = prettyPaint().also {
    it.textSize = 32f
    it.textAlign = Paint.Align.CENTER
    it.color = Color.BLACK
}

fun coloredGradient(center: Point, color: Int, radius: Float): Paint = prettyPaint().also {
    it.shader = RadialGradient(
        center.x.toFloat(),
        center.y.toFloat(),
        radius,
        color,
        Color.TRANSPARENT,
        CLAMP
    )
}

private fun prettyPaint(): Paint = Paint().also {
    it.isAntiAlias = true
}