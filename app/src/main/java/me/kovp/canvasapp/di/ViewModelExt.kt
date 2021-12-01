package me.kovp.canvasapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline fun <reified VM : ViewModel> ViewModelStoreOwner.injectViewModel(): ReadOnlyProperty<Any, VM> =
    ViewModelInjectorDelegate(
        viewModelStoreOwner = this,
        viewModelClass = VM::class.java
    )

class ViewModelInjectorDelegate<VM : ViewModel, O : ViewModelStoreOwner>(
    viewModelStoreOwner: O,
    viewModelClass: Class<VM>
) : ReadOnlyProperty<Any, VM> {
    private val fromToothpick: ViewModel by viewModelStoreOwner.inject(viewModelClass)

    private val fromStoreOwner: VM by lazy {
        ViewModelProvider(
            viewModelStoreOwner,
            ViewModelFactory { fromToothpick }
        )[viewModelClass]
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): VM = fromStoreOwner
}

class ViewModelFactory(
    private val vmProvider: (Class<out ViewModel>) -> ViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = vmProvider(modelClass) as T
}