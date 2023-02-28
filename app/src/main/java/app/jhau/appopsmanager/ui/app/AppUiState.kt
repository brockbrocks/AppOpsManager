package app.jhau.appopsmanager.ui.app
import app.jhau.appopsmanager.data.repository.PackageInfoRepository

data class AppUiState(
    val apps: List<AppItemUiState> = emptyList(),
    val sortType: PackageInfoRepository.SortType = PackageInfoRepository.SortType.UID,
    val filterTypes: Set<PackageInfoRepository.FilterType> = setOf(
        PackageInfoRepository.FilterType.USER_APP
    )
)

data class AppItemUiState(
    val uid: Int,
    val appName: String = "",
    val packageName: String = "",
    val disabled: Boolean
)