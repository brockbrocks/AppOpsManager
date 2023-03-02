package app.jhau.appopsmanager.ui.appperms

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PermissionInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppPermsViewModel @AssistedInject constructor(
    @Assisted pkgInfo: PackageInfo,
    private val app: Application
): ViewModel() {

    private val _appPermInfoList: MutableStateFlow<List<PermissionInfo>>

    init {
        val permInfoList = mutableListOf<PermissionInfo>()
        for (i in 0 until pkgInfo.requestedPermissions.size) {
            val permName = pkgInfo.requestedPermissions[i]
            val permInfo = app.packageManager.getPermissionInfo(permName, 0)
            permInfoList.add(permInfo)
        }
        _appPermInfoList = MutableStateFlow(permInfoList)
    }

    val appPermInfoList = _appPermInfoList.asStateFlow()

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(pkgInfo: PackageInfo): AppPermsViewModel
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(factory: AssistedFactory, pkgInfo: PackageInfo): ViewModelProvider.Factory {
            return object :ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return factory.create(pkgInfo) as T
                }
            }
        }
    }
}