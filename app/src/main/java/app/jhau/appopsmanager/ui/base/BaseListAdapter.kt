package app.jhau.appopsmanager.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BaseListAdapter<T, VB : ViewBinding> :
    Adapter<BaseListAdapter.ViewHolder<VB>>() {

    protected var items: List<T> = listOf()
    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<T>) {
        val oldItems = items
        items = newItems
        val areItemsSame: (T, T) -> Boolean = { oldItem, newItem ->
            areItemsSame(oldItem, newItem)
        }
        val artContentsTheSame: (T, T) -> Boolean = { oldItem, newItem ->
            areContentsTheSame(oldItem, newItem)
        }
        val diffUtilCallback =
            DiffUtilCallback(oldItems, newItems, areItemsSame, artContentsTheSame)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    abstract fun areItemsSame(oldItem: T, newItem: T): Boolean
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

    //Implementation of DiffUtil.Callback
    class DiffUtilCallback<T>(
        private val oldItems: List<T>,
        private val newItems: List<T>,
        private val areItemsTheSame: (T, T) -> Boolean,
        private val areContentsTheSame: (T, T) -> Boolean
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areItemsTheSame.invoke(oldItems[oldItemPosition], newItems[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areContentsTheSame.invoke(oldItems[oldItemPosition], newItems[newItemPosition])
        }
    }
}