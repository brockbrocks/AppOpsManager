package app.jhau.appopsmanager.ui.appops

import app.jhau.appopsmanager.databinding.ItemAppOpBinding
import app.jhau.appopsmanager.ui.base.BaseListAdapter

class AppOpsAdapter(
    private val onOpClick: (OpUiState) -> Unit
): BaseListAdapter<OpUiState, ItemAppOpBinding>() {
    override fun areItemsTheSame(oldItem: OpUiState, newItem: OpUiState): Boolean {
        return oldItem.op == newItem.op
    }

    override fun areContentsTheSame(oldItem: OpUiState, newItem: OpUiState): Boolean {
        return oldItem.mode == newItem.mode
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemAppOpBinding>, position: Int) {
        holder.binding.apply {
            val opUiState = items[position]
            op.text = opUiState.opStr
            opMode.text = opUiState.modeStr
            root.setOnClickListener { onOpClick(opUiState) }
        }
    }
}