package app.jhau.appopsmanager.ui.appperms

import android.content.pm.PermissionInfo
import app.jhau.appopsmanager.databinding.ItemAppPermsBinding
import app.jhau.appopsmanager.ui.base.BaseListAdapter

class AppPermsAdapter: BaseListAdapter<PermissionInfo, ItemAppPermsBinding>() {
    override fun areItemsTheSame(oldItem: PermissionInfo, newItem: PermissionInfo): Boolean {
        return true
    }

    override fun areContentsTheSame(oldItem: PermissionInfo, newItem: PermissionInfo): Boolean {
        return true
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemAppPermsBinding>, position: Int) {
        holder.binding.apply {
            val permInfo = items[position]
            permName.text = permInfo.name
//            isGranted.isChecked = permInfo.flags
        }
    }
}