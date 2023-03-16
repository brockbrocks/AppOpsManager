package app.jhau.appopsmanager.ui.appops

data class AppOpsUiState(
    val ops: List<OpUiState> = emptyList()
)

data class OpUiState(
    val op: Int,
    val opStr: String,
    val mode: Int,
    val modeStr: String
)