package app.jhau.appopsmanager.ui.appopsinfo

data class AppOpsInfoUiState(
    val ops: List<OpUiState> = emptyList()
)

data class OpUiState(
    val op: Int,
    val opStr: String,
    val mode: Int,
    val modeStr: String
)