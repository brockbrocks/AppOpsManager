package app.jhau.appopsmanager.ui.app

data class AppUiState(
    val apps: List<AppItemUiState> = emptyList(),
    val sortType: AppViewModel.SortType = AppViewModel.SortType.UID,
    val filterTypes: Set<AppViewModel.FilterType> = setOf(AppViewModel.FilterType.USER_APP),
    val searching: Boolean = false
)

data class AppItemUiState(
    val uid: Int,
    val appName: String = "",
    val packageName: String = "",
    val disabled: Boolean
)