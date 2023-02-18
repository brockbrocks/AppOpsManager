package app.jhau.appopsmanager.ui.app

data class AppUiState(
    val apps: List<AppItemUiState> = listOf()
)

data class AppItemUiState(
    val uid: Int,
    val appName: String = "",
    val packageName: String = "",
    val disabled: Boolean
)