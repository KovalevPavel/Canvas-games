package me.kovp.canvasapp.interactors

import android.graphics.Point
import me.kovp.canvasapp.models.AnimationParams
import me.kovp.canvasapp.models.CelestialSphere
import me.kovp.canvasapp.repos.AnimationRepo
import me.kovp.canvasapp.repos.GraphicsRepo
import toothpick.InjectConstructor

@InjectConstructor
class AnimInteractor(
    private val animRepo: AnimationRepo,
    private val graphicsRepo: GraphicsRepo
) {
    fun getBottomBorder() = graphicsRepo.getBottomBorder()

    fun computeDelays(params: AnimationParams) = animRepo.computeDelays(params)

    fun getDrawParams(oldPosition: Point, sphere: CelestialSphere, clockwise: Boolean = true) =
        graphicsRepo.getDrawParams(oldPosition, sphere, clockwise)
}