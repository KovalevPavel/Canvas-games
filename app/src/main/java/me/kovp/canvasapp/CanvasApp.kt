package me.kovp.canvasapp

import android.app.Application
import android.content.Context
import android.view.Display
import me.kovp.canvasapp.di.ApplicationScope
import toothpick.Scope
import toothpick.Toothpick
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

class CanvasApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDi()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        KTP.closeScope(ApplicationScope::class.java)
    }

    private fun initDi() {
        KTP.openScope(ApplicationScope::class.java).apply {
            installModules(
                module {
                    bind(Context::class).toInstance(this@CanvasApp)
                }
            )
            Toothpick.inject(this@CanvasApp, this)
        }
    }
}