package app.jhau.appopsmanager.ui.app

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
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
    private val pkgInfoRepo: PackageInfoRepository
) : ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState = _appUiState.asStateFlow()

    private lateinit var pkgs: MutableMap<String, PackageInfo>

    private fun sortLogic(pkg1: PackageInfo, pkg2: PackageInfo, sortType: SortType): Int {
        return when (sortType) {
            SortType.NAME -> {
                val pkg1Name = pkg1.applicationInfo.loadLabel(app.packageManager).toString()
                val pkg2Name = pkg2.applicationInfo.loadLabel(app.packageManager).toString()
                pkg1Name.compareTo(pkg2Name)
            }
            SortType.UID -> pkg1.applicationInfo.uid - pkg2.applicationInfo.uid
        }
    }

    private fun filterLogic(pkgInfo: PackageInfo, filterTypeSet: Set<FilterType>): Boolean {
        var filterOut = false
        val isSystemApp = (pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0

        if (filterTypeSet.contains(FilterType.USER_APP)) {
            filterOut = filterOut || !isSystemApp
        }

        if (filterTypeSet.contains(FilterType.SYSTEM_APP)) {
            filterOut = filterOut || isSystemApp
        }

        if (filterTypeSet.contains(FilterType.DISABLED)) {
            filterOut = filterOut || !pkgInfo.applicationInfo.enabled
        }

        return filterOut
    }

    fun setSortType(sortType: SortType) {
        viewModelScope.launch {
            val curFilterTypes = _appUiState.value.filterTypes
            val displayPkgs = pkgs.values.filter { filterLogic(it, curFilterTypes) }
                .sortedWith { o1, o2 -> sortLogic(o1, o2, sortType) }
            updateAppItemUiState(displayPkgs, sortType, curFilterTypes)
        }
    }

    fun addFilter(filterType: FilterType) {
        viewModelScope.launch {
            val curSortType = _appUiState.value.sortType
            val newFilterTypes = _appUiState.value.filterTypes.toMutableSet()
            newFilterTypes.add(filterType)
            val displayPkgs = pkgs.values.filter { filterLogic(it, newFilterTypes) }
                .sortedWith { o1, o2 -> sortLogic(o1, o2, curSortType) }
            updateAppItemUiState(displayPkgs, curSortType, newFilterTypes)
        }
    }

    fun removeFilter(filterType: FilterType) {
        viewModelScope.launch {
            val curSortType = _appUiState.value.sortType
            val newFilterTypes = _appUiState.value.filterTypes.toMutableSet()
            newFilterTypes.remove(filterType)
            val displayPkgs = pkgs.values.filter { filterLogic(it, newFilterTypes) }
                .sortedWith { o1, o2 -> sortLogic(o1, o2, curSortType) }
            updateAppItemUiState(displayPkgs, curSortType, newFilterTypes)
        }
    }

    fun fetchPackageInfoList(update: Boolean = false) {
        viewModelScope.launch {
            val fetchPkgs = pkgInfoRepo.getPackageInfoList(refresh = update)
            pkgs = linkedMapOf<String, PackageInfo>().apply {
                fetchPkgs.forEach { this[it.packageName] = it }
            }
            val sortType = _appUiState.value.sortType
            val filterTypes = _appUiState.value.filterTypes
            val displayPkgs = pkgs.values.filter { filterLogic(it, filterTypes) }
                .sortedWith { o1, o2 -> sortLogic(o1, o2, sortType) }
            updateAppItemUiState(displayPkgs, sortType, filterTypes)
        }
    }

    private suspend fun updateAppItemUiState(
        pkgs: List<PackageInfo>,
        sortType: SortType,
        filterTypes: Set<FilterType>,
        searching: Boolean = false
    ) {
        val tmpAppUiState: AppUiState = _appUiState.value
        val newAppItemUiStates = pkgs.map {
            AppItemUiState(
                it.applicationInfo.uid,
                it.applicationInfo.loadLabel(app.packageManager).toString(),
                it.packageName,
                it.applicationInfo.enabled
            )
        }
        val newAppUiState = tmpAppUiState.copy(
            apps = newAppItemUiStates,
            sortType = sortType,
            filterTypes = filterTypes,
            searching = searching
        )
        _appUiState.emit(newAppUiState)
    }

    fun getPackageInfo(pkgName: String) = pkgs[pkgName]

    fun loadIcon(pkgName: String): Drawable? {
        val pkg = pkgs[pkgName] ?: return null
        return pkg.applicationInfo.loadIcon(app.packageManager)
    }

    fun searchApp(content: String) {
        viewModelScope.launch {
            val retPkgs = pkgs.values.filter {
                it.packageName.contains(content)
                        || it.applicationInfo.uid.toString().contains(content)
                        || it.versionName.contains(content)
                        || it.applicationInfo.loadLabel(app.packageManager).contains(content)
            }
            val newAppItemUiStates = retPkgs.map {
                AppItemUiState(
                    it.applicationInfo.uid,
                    it.applicationInfo.loadLabel(app.packageManager).toString(),
                    it.packageName,
                    it.applicationInfo.enabled
                )
            }
            _appUiState.emit(_appUiState.value.copy(apps = newAppItemUiStates, searching = true))
        }
    }

    fun clearSearch() {
        viewModelScope.launch {
            val sortType = _appUiState.value.sortType
            val filterTypes = _appUiState.value.filterTypes
            val displayPkgs = pkgs.values.filter { filterLogic(it, filterTypes) }
                .sortedWith { o1, o2 -> sortLogic(o1, o2, sortType) }
            updateAppItemUiState(
                displayPkgs,
                sortType,
                filterTypes,
                false
            )
        }
    }

    enum class SortType {
        NAME, UID
    }

    enum class FilterType {
        USER_APP, SYSTEM_APP, DISABLED
    }
}