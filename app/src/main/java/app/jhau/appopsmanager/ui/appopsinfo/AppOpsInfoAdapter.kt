package app.jhau.appopsmanager.ui.appopsinfo

import app.jhau.appopsmanager.databinding.ItemAppopsInfoListBinding
import app.jhau.appopsmanager.ui.base.BaseListAdapter

class AppOpsInfoAdapter(
    private val onItemClickListener: (OpUiState, Int) -> Unit
) : BaseListAdapter<OpUiState, ItemAppopsInfoListBinding>() {
    override fun areItemsTheSame(oldItem: OpUiState, newItem: OpUiState): Boolean {
        return oldItem.op == newItem.op
    }

    override fun areContentsTheSame(oldItem: OpUiState, newItem: OpUiState): Boolean {
        return oldItem.opStr == newItem.opStr &&
                oldItem.mode == newItem.mode &&
                oldItem.modeStr == newItem.modeStr
    }

    override fun onBindViewHolder(holder: ViewHolder<ItemAppopsInfoListBinding>, position: Int) {
        holder.binding.apply {
            val opUiState = items[position]
            op.text = "${opUiState.opStr}(${opUiState.op})"
            opMode.text = "${opUiState.modeStr}"
            opMode.setOnClickListener { onItemClickListener.invoke(opUiState, position) }
        }
        holder.binding.opContainer.setOnClickListener {

        }
    }

}