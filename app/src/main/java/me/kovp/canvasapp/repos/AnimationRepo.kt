package me.kovp.canvasapp.repos

import me.kovp.canvasapp.models.AnimationParams
import me.kovp.canvasapp.ui.AnimationParams.ANIMATION_STEP
import toothpick.InjectConstructor

@InjectConstructor
class AnimationRepo {

    fun computeDelays(params: AnimationParams): List<Long> {
        val resultList = mutableListOf<Long>()

        val duration = (params.overallDuration ?: DEF_OVERALL_DURATION)
        val dayDuration = duration * (params.dayDuration ?: DEF_DAY_DURATION)

        (dayDuration * ANIMATION_STEP * 1000).toLong()
            .let(resultList::add)
        ((duration - dayDuration) * ANIMATION_STEP * 1000).toLong()
            .let(resultList::add)
        return resultList
    }

    companion object {
        private const val DEF_OVERALL_DURATION = 20
        private const val DEF_DAY_DURATION = 0.5
    }
}