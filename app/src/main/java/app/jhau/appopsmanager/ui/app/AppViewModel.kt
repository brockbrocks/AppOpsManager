package app.jhau.appopsmanager.ui.app

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.jhau.appopsmanager.data.repository.PackageInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val app: Application,
    private val packageInfoRepository: PackageInfoRepository
) : ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState = _appUiState.asStateFlow()

    private lateinit var packageInfoList: List<PackageInfo>

    fun setSortType(sortType: PackageInfoRepository.SortType) {
        val curFilterTypes = _appUiState.value.filterTypes
        fetchPackageInfoList(sortType = sortType, filterTypes = curFilterTypes)
    }

    fun addFilter(filterType: PackageInfoRepository.FilterType) {
        val newFilterTypes = _appUiState.value.filterTypes.toMutableSet()
        newFilterTypes.add(filterType)
        fetchPackageInfoList(_appUiState.value.sortType, newFilterTypes)
    }

    fun removeFilter(filterType: PackageInfoRepository.FilterType) {
        val newFilterTypes = _appUiState.value.filterTypes.toMutableSet()
        newFilterTypes.remove(filterType)
        fetchPackageInfoList(_appUiState.value.sortType, newFilterTypes)
    }

    fun fetchPackageInfoList(
        sortType: PackageInfoRepository.SortType,
        filterTypes: Set<PackageInfoRepository.FilterType>
    ) = viewModelScope.launch {
        packageInfoList = packageInfoRepository.fetchPackageInfoList(PackageManager.GET_PERMISSIONS, sortType, filterTypes)
        updateAppItemUiState(packageInfoList, sortType, filterTypes)
    }

    private suspend fun updateAppItemUiState(
        pkgInfoList: List<PackageInfo>,
        sortType: PackageInfoRepository.SortType,
        filterTypes: Set<PackageInfoRepository.FilterType>
    ) {
        var tmpAppUiState: AppUiState = _appUiState.value
        val newAppItemUiStates = pkgInfoList.map {
            AppItemUiState(
                it.applicationInfo.uid,
                it.applicationInfo.loadLabel(app.packageManager).toString(),
                it.packageName,
                it.applicationInfo.enabled
            )
        }
        tmpAppUiState = tmpAppUiState.copy(apps = newAppItemUiStates, sortType = sortType, filterTypes = filterTypes)
        _appUiState.emit(tmpAppUiState)
    }

    fun getPackageInfo(position: Int) = packageInfoList[position]
    fun getAppIcon(pkgName: String): Drawable {
        val pkgInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            packageInfoRepository.fetchPackageInfo(
                pkgName,
                PackageManager.MATCH_UNINSTALLED_PACKAGES.toLong()
            )
        } else {
            packageInfoRepository.fetchPackageInfo(pkgName, 0)
        }
        return pkgInfo.applicationInfo.loadIcon(app.packageManager)
    }

    fun searchApp(content: String) = viewModelScope.launch {
        val ret = _appUiState.value.apps.filter {
            it.uid.toString().contains(content) ||
                    it.appName.contains(content) ||
                    it.packageName.contains(content)
        }
        _appUiState.emit(_appUiState.value.copy(apps = ret))
    }

    fun clearSearch() = viewModelScope.launch{
        updateAppItemUiState(packageInfoList, _appUiState.value.sortType, _appUiState.value.filterTypes)
    }

}