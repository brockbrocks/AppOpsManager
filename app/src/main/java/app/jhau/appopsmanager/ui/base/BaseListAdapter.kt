package app.jhau.appopsmanager.ui.base

import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import java.lang.reflect.ParameterizedType
import java.util.concurrent.Executors

abstract class BaseListAdapter<T, VB : ViewBinding> :
    Adapter<BaseListAdapter.ViewHolder<VB>>() {

    protected var items: List<T> = listOf()
    override fun getItemCount(): Int = items.size

    private val mH = android.os.Handler(Looper.getMainLooper())
    private val mDiffExecutor = Executors.newFixedThreadPool(2)
    fun submitList(newItems: List<T>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = items.size

            override fun getNewListSize() = newItems.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areItemsTheSame(items[oldItemPosition], newItems[newItemPosition])
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areContentsTheSame(items[oldItemPosition], newItems[newItemPosition])
            }
        }
        mDiffExecutor.execute {
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            mH.post {
                items = newItems
                diffResult.dispatchUpdatesTo(this)
            }
        }
    }

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean
    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<VB> {
        return ViewHolder(reflectAndCreateViewBinding(parent, this))
    }

    @Suppress("UNCHECKED_CAST")
    private fun reflectAndCreateViewBinding(
        parent: ViewGroup,
        listAdapterImpl: BaseListAdapter<T, VB>
    ): VB {
        val type: ParameterizedType =
            listAdapterImpl::class.java.genericSuperclass as ParameterizedType
        val bindingType = type.actualTypeArguments[1] as Class<VB>
        val inflateMethod = bindingType.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        val layoutInflater = LayoutInflater.from(parent.context)
        return inflateMethod.invoke(null, layoutInflater, parent, false) as VB
    }

    //ViewBinding ViewHolder
    class ViewHolder<VB : ViewBinding>(val binding: VB) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)
}