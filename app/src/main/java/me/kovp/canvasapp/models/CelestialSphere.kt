package me.kovp.canvasapp.models

import android.graphics.Color

sealed class CelestialSphere(
    val color: Int,
    val radius: Int = 60,
    val text: String
) {
    class Moon : CelestialSphere(
        color = Color.parseColor("#ECAE5A"),
        text = "MOON"
    )

    class Sun : CelestialSphere(
        color = Color.parseColor("#FBFF0F"),
        text = "SUN"
    )
}