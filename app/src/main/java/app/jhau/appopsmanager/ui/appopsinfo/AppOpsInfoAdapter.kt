package app.jhau.appopsmanager.ui.appopsinfo

import app.jhau.appopsmanager.databinding.ItemAppopsInfoListBinding
import app.jhau.appopsmanager.ui.base.BaseListAdapter

class AppOpsInfoAdapter(
    private val onItemClickListener: (OpUiState, Int) -> Unit
) : BaseListAdapter<OpUiState, ItemAppopsInfoListBinding>() {
    override fun areItemsTheSame(oldItem: OpUiState, newItem: OpUiState): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: OpUiState, newItem: OpUiState): Boolean {
        return oldItem.op == newItem.op &&
                oldItem.opStr == newItem.opStr &&
                oldItem.mode == newItem.mode &&
                oldItem.modeStr == newItem.modeStr
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemAppopsInfoListBinding>, position: Int) {
        holder.binding.apply {
            val opUiState = items[position]
            title.text = "${opUiState.opStr}"
            summary.text = "${opUiState.modeStr}"
            root.setOnClickListener { onItemClickListener.invoke(opUiState, position) }
        }
    }

}