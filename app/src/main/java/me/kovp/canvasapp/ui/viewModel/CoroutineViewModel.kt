@file:JvmName("AnimationViewModelKt")

package me.kovp.canvasapp.ui.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

abstract class CoroutineViewModel : ViewModel() {
    private var currentJob: Job? = null

    fun Job.forceStart() {
        currentJob?.cancel()
        currentJob = this
        currentJob?.start()
    }
}