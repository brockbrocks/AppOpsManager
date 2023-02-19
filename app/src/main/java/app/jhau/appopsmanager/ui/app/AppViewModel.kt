package app.jhau.appopsmanager.ui.app

import android.app.Application
import android.content.pm.PackageInfo
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.jhau.appopsmanager.data.repository.PackageInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val app: Application,
    private val packageInfoRepository: PackageInfoRepository
) : ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState = _appUiState.asStateFlow()

    private lateinit var packageInfoList: Array<PackageInfo>

    fun fetchPackageInfoList() = viewModelScope.launch {
        packageInfoList = packageInfoRepository.fetchPackageInfoList(0L)
        updateAppItemUiStateList(packageInfoList)
    }

    private suspend fun updateAppItemUiStateList(packageInfoList: Array<PackageInfo>) =
        withContext(Dispatchers.IO) {
            val appItemUiStateList = packageInfoList.map {
                AppItemUiState(
                    it.applicationInfo.uid,
                    app.packageManager.getApplicationLabel(it.applicationInfo).toString(),
                    it.packageName,
                    it.applicationInfo.enabled
                )
            }
            val oldValue = _appUiState.value
            _appUiState.emit(oldValue.copy(apps = appItemUiStateList))
        }

    fun getPackageInfo(position: Int) = packageInfoList[position]
    fun getAppIcon(pkgName: String): Drawable {
        val pkgInfo = packageInfoRepository.fetchPackageInfo(pkgName, 0)
        return app.packageManager.getApplicationIcon(pkgInfo.applicationInfo)
    }

}