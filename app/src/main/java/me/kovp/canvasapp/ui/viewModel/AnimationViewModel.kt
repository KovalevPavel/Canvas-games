package me.kovp.canvasapp.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.kovp.canvasapp.interactors.AnimInteractor
import me.kovp.canvasapp.models.AnimationParams
import me.kovp.canvasapp.models.CelestialSphere
import me.kovp.canvasapp.models.CelestialSphere.Moon
import me.kovp.canvasapp.models.CelestialSphere.Sun
import me.kovp.canvasapp.models.DrawParams
import me.kovp.canvasapp.utils.orDefault
import me.kovp.canvasapp.utils.orEmpty
import toothpick.InjectConstructor
import kotlin.properties.Delegates

interface AnimationViewModel {
    val sphere: LiveData<DrawParams>
    fun startAnimWithParams(params: AnimationParams)
    fun onSingleClick()
    fun onDoubleClick()
}

@InjectConstructor
class AnimationViewModelImpl(
    private val animInteractor: AnimInteractor
) : CoroutineViewModel(), AnimationViewModel {
    override val sphere = MutableLiveData<DrawParams>()

    private var currentSphere: CelestialSphere = Sun()
    private var bottomBorder by Delegates.notNull<Double>()
    private var clockwise: Boolean = true

    private var dayDelay: Long = 50
    private var nightDelay: Long = 50
    private var currentDelay: Long = 0

    override fun startAnimWithParams(params: AnimationParams) {
        getBottomBorder()
        initDelays(params)
        launchAnimation()
    }

    override fun onSingleClick() {
        clockwise = !clockwise
    }

    override fun onDoubleClick() {
        changeSphere()
    }

    private fun getBottomBorder() {
        bottomBorder = animInteractor.getBottomBorder() + (sphere.value?.sphere.orDefault()).radius
    }

    private fun initDelays(params: AnimationParams) {
        animInteractor.computeDelays(params).let { delays ->
            delays.firstOrNull()?.let {
                dayDelay = it
                currentDelay = it
            }
            delays.getOrNull(1)?.let { nightDelay = it }
        }
    }

    private fun launchAnimation() {
        viewModelScope.launch {
            while (isActive) {
                do {
                    animInteractor.getDrawParams(
                        oldPosition = sphere.value?.drawPoint.orEmpty(),
                        sphere = currentSphere,
                        clockwise = clockwise
                    ).let(sphere::postValue)
                    delay(currentDelay)
                } while ((sphere.value?.drawPoint.orEmpty()).y <= bottomBorder)
                changeSphere()
            }
        }.forceStart()
    }

    private fun changeSphere() = when (currentSphere) {
        is Sun -> {
            currentDelay = nightDelay
            currentSphere = Moon()
        }
        is Moon -> {
            currentDelay = dayDelay
            currentSphere = Sun()
        }
    }
}