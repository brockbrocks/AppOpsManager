package app.jhau.appopsmanager.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class ViewModelActivity<VB : ViewBinding, VM : ViewModel> : DataBindingActivity<VB>() {
    protected open val viewModel: VM by lazy {
        ViewModelProvider(this)[reflectViewModel(this)]
    }

    @Suppress("UNCHECKED_CAST")
    private fun reflectViewModel(activity: ViewModelActivity<VB, VM>): Class<VM> {
        val type: ParameterizedType = activity::class.java.genericSuperclass as ParameterizedType
        return type.actualTypeArguments[1] as Class<VM>
    }
}