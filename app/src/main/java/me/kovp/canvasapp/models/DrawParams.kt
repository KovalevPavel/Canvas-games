package me.kovp.canvasapp.models

import android.graphics.Point

data class DrawParams(
    val drawPoint: Point,
    val sphere: CelestialSphere,
    val backGround: BackgroundParams
) {
    data class BackgroundParams(
        val width: Float,
        val height: Float,
        val color: Int
    )
}
