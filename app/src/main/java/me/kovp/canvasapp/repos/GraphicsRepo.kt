package me.kovp.canvasapp.repos

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import me.kovp.canvasapp.R
import me.kovp.canvasapp.models.CelestialSphere
import me.kovp.canvasapp.models.CelestialSphere.Moon
import me.kovp.canvasapp.models.CelestialSphere.Sun
import me.kovp.canvasapp.models.DrawParams
import me.kovp.canvasapp.models.DrawParams.BackgroundParams
import me.kovp.canvasapp.ui.AnimationParams
import toothpick.InjectConstructor
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

@InjectConstructor
class GraphicsRepo(
    context: Context
) {
    //размеры области отрисовки
    private val maxCoordinateX = context.resources.displayMetrics.widthPixels
    private val maxCoordinateY = context.resources.getDimension(R.dimen.bitmapHeight)

    //координаты центра и радиус окружности
    private val cX = maxCoordinateX / 2
    private val cY =
        (maxCoordinateY.toDouble().pow(2) + (maxCoordinateX / 2.0).pow(2)) / (2 * maxCoordinateY)
    private val r0 = cY

    //угол, на который поворачивается сфера и угол старта
    private val maxAngle = 2 * asin((maxCoordinateX / 2.0) / r0)
    private val startAngle = asin((cY - maxCoordinateY) / r0)
    private var currentAngle = startAngle

    fun getBottomBorder(): Double {
        return cY - r0 * sin(startAngle)
    }

    fun getDrawParams(
        oldPosition: Point,
        sphere: CelestialSphere,
        clockwise: Boolean
    ): DrawParams {
        val newPoint = getNewPoint(oldPosition, sphere, clockwise)
        val height = (maxCoordinateY - newPoint.y) / maxCoordinateY
        return DrawParams(
            drawPoint = newPoint,
            sphere = sphere,
            backGround = BackgroundParams(
                width = maxCoordinateX.toFloat(),
                height = maxCoordinateY,
                color = getBackgroundColor(sphere, height.coerceIn(0f, 1f))
            )
        )
    }

    private fun getNewPoint(
        oldPosition: Point,
        sphere: CelestialSphere,
        clockwise: Boolean
    ): Point {
        clearStartPosition(
            oldPosition = oldPosition,
            sphere = sphere,
            clockwise = clockwise
        )
        val angle = if (clockwise) currentAngle + AnimationParams.ANIMATION_STEP * maxAngle
        else currentAngle - AnimationParams.ANIMATION_STEP * maxAngle
        currentAngle = angle

        val newX = cX - r0 * cos(angle)
        val newY = cY + sphere.radius - r0 * sin(angle)
        return Point(newX.toInt(), newY.toInt())
    }

    private fun getBackgroundColor(sphere: CelestialSphere, height: Float): Int {
        val colors = getGradientColors(sphere)
        val startRed = colors.first.red
        val startGreen = colors.first.green
        val startBlue = colors.first.blue

        val endRed = colors.second.red
        val endGreen = colors.second.green
        val endBlue = colors.second.blue

        val newRed = startRed + height * (endRed - startRed)
        val newGreen = startGreen + height * (endGreen - startGreen)
        val newBlue = startBlue + height * (endBlue - startBlue)

        return Color.rgb(newRed.toInt(), newGreen.toInt(), newBlue.toInt())
    }

    private fun getGradientColors(sphere: CelestialSphere): Pair<Int, Int> = when (sphere) {
        is Sun -> Color.BLACK to Color.WHITE
        is Moon -> Color.MAGENTA to Color.BLUE
    }

    private fun clearStartPosition(
        oldPosition: Point,
        sphere: CelestialSphere,
        clockwise: Boolean
    ) {
        if (oldPosition.y > cY + sphere.radius) {
            currentAngle = if (clockwise) startAngle else Math.PI - startAngle
        }
    }
}