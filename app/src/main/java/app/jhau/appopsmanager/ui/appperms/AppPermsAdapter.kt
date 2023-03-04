package app.jhau.appopsmanager.ui.appperms

import android.content.pm.PermissionInfo
import app.jhau.appopsmanager.databinding.ItemAppPermsBinding
import app.jhau.appopsmanager.ui.base.BaseListAdapter

class AppPermsAdapter(
    private val onPermsChecked: (String, Boolean) -> Unit,
    private val checkGranted: (String) -> Boolean
) : BaseListAdapter<Pair<String, PermissionInfo?>, ItemAppPermsBinding>() {
    override fun areItemsTheSame(
        oldItem: Pair<String, PermissionInfo?>,
        newItem: Pair<String, PermissionInfo?>
    ): Boolean {
        return oldItem.first == newItem.first
    }

    override fun areContentsTheSame(
        oldItem: Pair<String, PermissionInfo?>,
        newItem: Pair<String, PermissionInfo?>
    ): Boolean {
        return oldItem.second == newItem.second
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemAppPermsBinding>, position: Int) {
        val (permName, permInfo) = items[position]
        holder.binding.apply {
            this.permName.text = permName
            isGranted.isChecked = checkGranted.invoke(permName)
        }
        holder.binding.isGranted.setOnCheckedChangeListener { buttonView, isChecked ->
            onPermsChecked(permName, isChecked)
        }
    }

    override fun onViewRecycled(holder: ViewHolder<ItemAppPermsBinding>) {
        super.onViewRecycled(holder)
        holder.binding.isGranted.setOnCheckedChangeListener(null)
    }
}