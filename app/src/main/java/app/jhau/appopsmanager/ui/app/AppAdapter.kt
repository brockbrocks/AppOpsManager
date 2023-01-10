package app.jhau.appopsmanager.ui.app

import app.jhau.appopsmanager.databinding.ItemAppListBinding
import app.jhau.appopsmanager.ui.appdetail.AppDetailActivity
import app.jhau.appopsmanager.ui.base.BaseListAdapter

class AppAdapter : BaseListAdapter<AppItemUiState, ItemAppListBinding>() {

    override fun areItemsTheSame(oldItem: AppItemUiState, newItem: AppItemUiState): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AppItemUiState, newItem: AppItemUiState): Boolean {
        return oldItem == newItem
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemAppListBinding>, position: Int) {
        holder.binding.apply {
            val appItemUiState = items[position]
            ivApp.setImageDrawable(appItemUiState.icon)
            tvAppName.text = appItemUiState.appName
            tvApplicationId.text = appItemUiState.packageName
            root.setOnClickListener {
                AppDetailActivity.start(holder.itemView.context, items[position])
            }
        }
    }

}
