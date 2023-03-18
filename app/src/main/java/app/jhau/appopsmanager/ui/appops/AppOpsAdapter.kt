package app.jhau.appopsmanager.ui.appops

import android.annotation.SuppressLint
import android.view.View
import app.jhau.appopsmanager.databinding.ItemAppOpBinding
import app.jhau.appopsmanager.ui.base.BaseListAdapter
import app.jhau.appopsmanager.util.dateStr
import java.util.*

class AppOpsAdapter(
    private val onOpClick: (OpUiState) -> Unit,
    private val onOpLongClick: (OpUiState) -> Boolean
) : BaseListAdapter<OpUiState, ItemAppOpBinding>() {
    override fun areItemsTheSame(oldItem: OpUiState, newItem: OpUiState): Boolean {
        return oldItem.op == newItem.op
    }

    override fun areContentsTheSame(oldItem: OpUiState, newItem: OpUiState): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder<ItemAppOpBinding>, position: Int) {
        holder.binding.apply {
            val opUiState = items[position]
            uidMode.visibility = if (opUiState.uidMode) View.VISIBLE else View.GONE
            opName.text = opUiState.opName
            opMode.text = opUiState.modeStr
            if (opUiState.lastAccessTime > 0) {
                lastAccessTime.visibility = View.VISIBLE
                lastAccessTime.text =
                    "last access: " + opUiState.lastAccessTime.dateStr("yyyy/M/dd HH:mm:ss")
            } else {
                lastAccessTime.visibility = View.GONE
            }
            if (opUiState.lastRejectTime > 0) {
                lastRejectTime.visibility = View.VISIBLE
                lastRejectTime.text =
                    "last reject: " + opUiState.lastRejectTime.dateStr("yyyy/M/dd HH:mm:ss")
            } else {
                lastRejectTime.visibility = View.GONE
            }
            root.setOnClickListener { onOpClick(opUiState) }
            root.setOnLongClickListener { onOpLongClick(opUiState) }
        }
    }
}