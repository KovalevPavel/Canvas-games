package me.kovp.canvasapp.utils

import android.graphics.Point
import me.kovp.canvasapp.models.CelestialSphere
import me.kovp.canvasapp.models.CelestialSphere.Sun

fun Point?.orEmpty() = this ?: Point(0, 0)

fun CelestialSphere?.orDefault() = this ?: Sun()
