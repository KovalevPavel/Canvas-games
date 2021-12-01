package me.kovp.canvasapp.ui

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.View.OnClickListener
import java.util.*

class DoubleClickListener(
    private val onSingleClick: () -> Unit,
    private val onDoubleClick: () -> Unit
) : OnClickListener {
    private var timer: Timer? = null
    private var lastClickTime = 0L

    override fun onClick(p0: View?) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime <= DOUBLE_CLICK_DELAY) processDoubleClick()
        else processSingleClick()
        lastClickTime = clickTime
    }

    private fun processSingleClick() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable { onSingleClick() }

        val timerTask = object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }
        timer = Timer().also {
            it.schedule(timerTask, SINGLE_CLICK_DELAY)
        }
    }

    private fun processDoubleClick() {
        timer?.let {
            it.cancel()
            it.purge()
        }
        onDoubleClick()
    }

    companion object {
        private const val DOUBLE_CLICK_DELAY = 300L
        private const val SINGLE_CLICK_DELAY = 400L
    }
}