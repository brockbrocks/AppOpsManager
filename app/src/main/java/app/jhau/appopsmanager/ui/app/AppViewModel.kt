package app.jhau.appopsmanager.ui.app

import android.app.Application
import android.content.pm.PackageInfo
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

    private lateinit var packageInfoList: Array<PackageInfo>

    init {
        viewModelScope.launch {
            packageInfoList = packageInfoRepository.fetchPackageInfoList(0L)
            updateAppItemUiStateList(packageInfoList)
        }
    }

    private suspend fun updateAppItemUiStateList(packageInfoList: Array<PackageInfo>) {
        val appItemUiStateList = packageInfoList.map {
            AppItemUiState(
                app.packageManager.getApplicationLabel(it.applicationInfo).toString(),
                it.packageName,
                app.packageManager.getApplicationIcon(it.applicationInfo)
            )
        }
        val oldValue = _appUiState.value
        _appUiState.emit(oldValue.copy(apps = appItemUiStateList))
    }

    fun getPackageInfo(position: Int) = packageInfoList[position]

}