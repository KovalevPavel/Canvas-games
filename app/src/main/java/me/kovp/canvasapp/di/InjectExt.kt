package me.kovp.canvasapp.di

import toothpick.ktp.KTP
import toothpick.ktp.delegate.EagerDelegate
import toothpick.ktp.delegate.InjectDelegate

fun <T: Any> Any.inject (
    clazz: Class<T>
): InjectDelegate<T> = EagerDelegate (clazz, null).also {
    KTP.delegateNotifier.registerDelegate(this, it)
}