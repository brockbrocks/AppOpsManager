package app.jhau.appopsmanager.ui.appperms

import android.content.pm.PackageInfo
import android.content.pm.PermissionInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.jhau.appopsmanager.data.repository.PermissionInfoRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppPermsViewModel @AssistedInject constructor(
    @Assisted val pkgInfo: PackageInfo,
    private val permsRepository: PermissionInfoRepository
) : ViewModel() {
    private val pkgName = pkgInfo.packageName
    fun setPermission(permName: String, isGranted: Boolean) = viewModelScope.launch {
        permsRepository.setPermissionGranted(pkgName, permName, isGranted)
        updateAppPermList()
    }

    private val _appPermInfoList: MutableStateFlow<List<Pair<String, PermissionInfo?>>>

    init {
        val perms = permsRepository.fetchPermissionInfoList(
            pkgInfo.requestedPermissions,
            pkgInfo.requestedPermissionsFlags,
            true
        )
        _appPermInfoList = MutableStateFlow(perms)
    }

    val appPermInfoList = _appPermInfoList.asStateFlow()

    private suspend fun updateAppPermList() {
        val perms = permsRepository.fetchPermissionInfoList(
            pkgInfo.requestedPermissions,
            pkgInfo.requestedPermissionsFlags,
            true
        )
        _appPermInfoList.emit(perms)
    }

    fun checkGranted(permName: String): Boolean {
        return permsRepository.isGranted(permName)
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(pkgInfo: PackageInfo): AppPermsViewModel
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(
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