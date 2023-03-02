package app.jhau.appopsmanager.ui.appinfo

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.jhau.appopsmanager.data.repository.PackageInfoRepository
import app.jhau.server.IServerManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppInfoViewModel @AssistedInject constructor(
    @Assisted pkgInfo: PackageInfo,
    private val packageInfoRepository: PackageInfoRepository,
    private val iServerManager: IServerManager
) : ViewModel() {

    private val _pkgInfo = MutableStateFlow(pkgInfo)
    val pkgInfo = _pkgInfo.asStateFlow()
    val onSetAppEnableSetting: MutableSharedFlow<Boolean> = MutableSharedFlow()

    fun startPermissionControllerByADB(pkgInfo: PackageInfo) = viewModelScope.launch {
        val resolveInfoList = packageInfoRepository.findPermissionControllerInfo()
        if (resolveInfoList.isNotEmpty() && resolveInfoList.size == 1) {
            val info = resolveInfoList[0]
            val pkgName = info.activityInfo.packageName
            val activityName = info.activityInfo.name
            val targetPkgName = pkgInfo.packageName
            val cmd =
                "am start -a android.intent.action.MANAGE_APP_PERMISSIONS --es android.intent.extra.PACKAGE_NAME ${targetPkgName} ${pkgName}/${activityName}"
            iServerManager.execCommand(cmd)
        }
    }

    fun switchAppEnableState() = viewModelScope.launch {
        val pkgName = _pkgInfo.value.packageName
        val enable = _pkgInfo.value.applicationInfo.enabled
        val newState =
            if (enable) {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            }
        try {
            val newPkgInfo = packageInfoRepository.setPackageEnable(pkgName, newState)
            _pkgInfo.emit(newPkgInfo)
            onSetAppEnableSetting.emit(newPkgInfo.applicationInfo.enabled)
        } catch (e: Throwable) {
            e.printStackTrace()
            return@launch
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(pkgInfo: PackageInfo): AppInfoViewModel
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideViewModelFactory(
            factory: AssistedFactory,
            pkgInfo: PackageInfo
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(pkgInfo) as T
                }
            }
        }
    }
}