package app.jhau.appopsmanager.ui.app

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import app.jhau.appopsmanager.BuildConfig
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ItemAppBinding
import app.jhau.appopsmanager.ui.base.BaseListAdapter

class AppAdapter(
    private val onAppClick: (String) -> Unit,
    private val onLoadIcon: (String, ImageView) -> Unit
) : BaseListAdapter<AppItemUiState, ItemAppBinding>() {
    var loadOptimize = false

    private var blankIcon: Drawable? = null

    override fun areItemsTheSame(oldItem: AppItemUiState, newItem: AppItemUiState): Boolean {
        return oldItem.uid == newItem.uid
                && oldItem.packageName == newItem.packageName
    }

    override fun areContentsTheSame(oldItem: AppItemUiState, newItem: AppItemUiState): Boolean {
        return oldItem.appName == newItem.appName
                && oldItem.disabled == newItem.disabled
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<ItemAppBinding> {
        val holder = super.onCreateViewHolder(parent, viewType)
        holder.binding.appIcon.setImageDrawable(
            AppCompatResources.getDrawable(
                parent.context,
                R.drawable.bg_app_icon_blank
            )
        )
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemAppBinding>, pos: Int) {
        holder.binding.apply {
            val appItemUiState = items[pos]
            holder.itemView.tag = appItemUiState.packageName
            if (!loadOptimize) onLoadIcon(appItemUiState.packageName, appIcon)
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
        if (blankIcon == null) {
            blankIcon = AppCompatResources.getDrawable(holder.itemView.context, R.drawable.bg_app_icon_blank)
        }
        holder.binding.appIcon.setImageDrawable(blankIcon)
    }

}
