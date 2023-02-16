package app.jhau.appopsmanager.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class DataBindingActivity<VB : ViewBinding> : AppCompatActivity() {
    protected open val binding: VB by lazy { reflectAndCreateViewBinding(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    @Suppress("UNCHECKED_CAST")
    private fun reflectAndCreateViewBinding(activity: DataBindingActivity<VB>): VB {
        val type: ParameterizedType = activity::class.java.genericSuperclass as ParameterizedType
        val bindingType = type.actualTypeArguments[0] as Class<VB>
        val inflateMethod = bindingType.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        return inflateMethod.invoke(null, layoutInflater, null, false) as VB
    }
}