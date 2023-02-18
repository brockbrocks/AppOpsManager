package app.jhau.appopsmanager.ui.app

import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.OnClickListener
import app.jhau.appopsmanager.databinding.ItemAppListBinding
import app.jhau.appopsmanager.ui.base.BaseListAdapter

class AppAdapter(
    private val onClickListener: OnClickListener,
    private val onGetAppIcon: (String) -> Drawable
) : BaseListAdapter<AppItemUiState, ItemAppListBinding>() {

    override fun areItemsTheSame(oldItem: AppItemUiState, newItem: AppItemUiState): Boolean {
        return oldItem.uid == newItem.uid
                && oldItem.packageName == newItem.packageName
    }

    override fun areContentsTheSame(oldItem: AppItemUiState, newItem: AppItemUiState): Boolean {
        return oldItem.appName == newItem.appName
                && oldItem.disabled == newItem.disabled
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemAppListBinding>, position: Int) {
        holder.binding.apply {
            val appItemUiState = items[position]
            val icon = onGetAppIcon(appItemUiState.packageName)
            appIcon.setImageDrawable(icon)
            appName.text = appItemUiState.appName
            applicationId.text = appItemUiState.packageName
            disabled.visibility = if (appItemUiState.disabled) View.GONE else View.VISIBLE
            root.setOnClickListener {
                onClickListener.onClick(this.root)
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder<ItemAppListBinding>) {
        super.onViewRecycled(holder)
        holder.binding.appIcon.setImageDrawable(null)
    }

}
