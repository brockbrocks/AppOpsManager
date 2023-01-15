package app.jhau.appopsmanager.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * Subclasses must be annotated with @AndroidEntryPoint when ViewModel is injected
 */
abstract class BaseBindingActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {

    protected val binding: VB by lazy {
        reflectAndCreateViewBinding(this)
    }

    protected val viewModel: VM by lazy {
        ViewModelProvider(this)[reflectViewModel(this)]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    @Suppress("UNCHECKED_CAST")
    private fun reflectAndCreateViewBinding(implActivity: BaseBindingActivity<VB, VM>): VB {
        val type: ParameterizedType =
            implActivity::class.java.genericSuperclass as ParameterizedType
        val bindingType = type.actualTypeArguments[0] as Class<VB>
        val inflateMethod = bindingType.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return inflateMethod.invoke(null, layoutInflater, null, false) as VB
    }

    @Suppress("UNCHECKED_CAST")
    private fun reflectViewModel(implActivity: BaseBindingActivity<VB, VM>): Class<VM> {
        val type: ParameterizedType =
            implActivity::class.java.genericSuperclass as ParameterizedType
        return type.actualTypeArguments[1] as Class<VM>
    }
}