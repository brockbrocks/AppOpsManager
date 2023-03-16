package app.jhau.appopsmanager.ui.app

import android.graphics.drawable.Drawable
import android.view.View
import app.jhau.appopsmanager.BuildConfig
import app.jhau.appopsmanager.databinding.ItemAppBinding
import app.jhau.appopsmanager.ui.base.BaseListAdapter

class AppAdapter(
    private val onAppClick: (String) -> Unit,
    private val onLoadIcon: (String) -> Drawable?
) : BaseListAdapter<AppItemUiState, ItemAppBinding>() {

    override fun areItemsTheSame(oldItem: AppItemUiState, newItem: AppItemUiState): Boolean {
        return oldItem.uid == newItem.uid
                && oldItem.packageName == newItem.packageName
    }

    override fun areContentsTheSame(oldItem: AppItemUiState, newItem: AppItemUiState): Boolean {
        return oldItem.appName == newItem.appName
                && oldItem.disabled == newItem.disabled
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemAppBinding>, pos: Int) {
        holder.binding.apply {
            val appItemUiState = items[pos]
            val icon = onLoadIcon(appItemUiState.packageName)
            appIcon.setImageDrawable(icon)
            appName.text = if (BuildConfig.DEBUG) {
                appItemUiState.appName + " (UID:${appItemUiState.uid})"
            } else {
                appItemUiState.appName
            }
            applicationId.text = appItemUiState.packageName
            disabled.visibility = if (appItemUiState.disabled) View.GONE else View.VISIBLE
            root.setOnClickListener {
                onAppClick(appItemUiState.packageName)
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder<ItemAppBinding>) {
        super.onViewRecycled(holder)
        holder.binding.appIcon.setImageDrawable(null)
    }

}
