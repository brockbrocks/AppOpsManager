package app.jhau.appopsmanager.ui.activity.appdetail

import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ItemAppListBinding

class AppAdapter(private val mApplicationInfoList: MutableList<ApplicationInfo>) : Adapter<AppAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app_list, parent, false)
        return ViewHolder(ItemAppListBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val info = mApplicationInfoList[position]
            val pm = holder.itemView.context.packageManager
            ivApp.setImageDrawable(pm.getApplicationIcon(info))
            tvAppName.text = pm.getApplicationLabel(info)
            tvApplicationId.text = info.packageName

            root.setOnClickListener {
                AppDetailActivity.start(holder.itemView.context, info)
            }
        }
    }

    override fun getItemCount(): Int = mApplicationInfoList.size

    class ViewHolder(val binding: ItemAppListBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root)
}