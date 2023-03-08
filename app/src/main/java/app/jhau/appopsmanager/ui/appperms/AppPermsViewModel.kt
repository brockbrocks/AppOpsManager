package app.jhau.appopsmanager.ui.appperms

import android.content.pm.PackageInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.jhau.appopsmanager.data.Permission
import app.jhau.appopsmanager.data.repository.PermissionRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppPermsViewModel @AssistedInject constructor(
    @Assisted pkgInfo: PackageInfo,
    private val permsRepo: PermissionRepository
) : ViewModel() {
    private val pkgName = pkgInfo.packageName

    private val _appPermInfoList: MutableStateFlow<List<Permission>> = MutableStateFlow(emptyList())
    val appPermInfoList: StateFlow<List<Permission>> = _appPermInfoList.asStateFlow()

    init {
        viewModelScope.launch {
            val perms = permsRepo.getPackagePermissions(pkgName, refresh = true)
            _appPermInfoList.emit(perms)
        }
    }

    fun grantPermission(permName: String) = viewModelScope.launch {
        permsRepo.grantPermission(pkgName, permName)
        val perms = permsRepo.getPackagePermissions(pkgName)
        _appPermInfoList.emit(perms)
    }

    fun revokePermission(permName: String) = viewModelScope.launch {
        permsRepo.revokePermission(pkgName, permName)
        val perms = permsRepo.getPackagePermissions(pkgName)
        _appPermInfoList.emit(perms)
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