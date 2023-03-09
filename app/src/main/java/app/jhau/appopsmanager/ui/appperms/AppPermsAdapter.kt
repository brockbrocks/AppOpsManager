package app.jhau.appopsmanager.ui.appperms

import android.view.View
import app.jhau.appopsmanager.data.Permission
import app.jhau.appopsmanager.databinding.ItemAppPermsBinding
import app.jhau.appopsmanager.ui.base.BaseListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AppPermsAdapter(
    private val onPermsChecked: (String, Boolean) -> Unit
) : BaseListAdapter<Permission, ItemAppPermsBinding>() {
    override fun areItemsTheSame(
        oldItem: Permission,
        newItem: Permission
    ): Boolean {
        return true
    }

    override fun areContentsTheSame(
        oldItem: Permission,
        newItem: Permission
    ): Boolean {
        return oldItem == newItem
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemAppPermsBinding>, position: Int) {
        val perm = items[position]
        holder.binding.permName.text = perm.name
        holder.binding.protection.text = perm.protectionStr()
        holder.binding.systemFixed.text = if (perm.isSystemFixed()) " (systemfixed)" else ""
        holder.binding.isGranted.apply {
            isChecked = perm.granted
            visibility = if (perm.isRuntime()) View.VISIBLE else View.GONE
            val changeable = perm.isChangeable
            isEnabled = changeable
            if (changeable) {
                setOnCheckedChangeListener { _, isChecked -> onPermsChecked(perm.name, isChecked) }
            }
        }
        holder.binding.permItem.setOnClickListener {
            val ctx = holder.itemView.context
            val dialog = MaterialAlertDialogBuilder(ctx).setAdapter(PermDetailAdapter(ctx, perm.toStringArray()), null).create()
            dialog.show()
        }
    }

    override fun onViewRecycled(holder: ViewHolder<ItemAppPermsBinding>) {
        super.onViewRecycled(holder)
        holder.binding.isGranted.setOnCheckedChangeListener(null)
    }
}