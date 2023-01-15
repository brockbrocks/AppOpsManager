package app.jhau.appopsmanager.ui.app

import android.view.View.OnClickListener
import app.jhau.appopsmanager.databinding.ItemAppListBinding
import app.jhau.appopsmanager.ui.base.BaseListAdapter

class AppAdapter(
    private val onClickListener: OnClickListener
) : BaseListAdapter<AppItemUiState, ItemAppListBinding>() {

    override fun areItemsTheSame(oldItem: AppItemUiState, newItem: AppItemUiState): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AppItemUiState, newItem: AppItemUiState): Boolean {
        return oldItem == newItem
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemAppListBinding>, position: Int) {
        holder.binding.apply {
            val appItemUiState = items[position]
            appIcon.setImageDrawable(appItemUiState.icon)
            appName.text = appItemUiState.appName
            applicationId.text = appItemUiState.packageName
            root.setOnClickListener {
                onClickListener.onClick(this.root)
            }
        }
    }

}
