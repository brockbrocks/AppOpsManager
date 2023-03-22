package app.jhau.appopsmanager.ui.appops

import android.content.pm.PackageInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.jhau.appopsmanager.data.repository.AppOpRepository
import app.jhau.server.IServerManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppOpsViewModel @AssistedInject constructor(
    @Assisted pkgInfo: PackageInfo,
    private val iServerManager: IServerManager,
    private val appOpRepo: AppOpRepository
) : ViewModel() {

    private val _appOpsUiState: MutableStateFlow<AppOpsUiState> = MutableStateFlow(AppOpsUiState())
    val appOpsInfoUiState: StateFlow<AppOpsUiState> = _appOpsUiState.asStateFlow()

    val uid: Int = pkgInfo.applicationInfo.uid
    val pkgName: String = pkgInfo.packageName

    val modeNames: Array<String> = iServerManager.appOpsManagerHidden.modeNames

    init {
        viewModelScope.launch {
            getAppOpList(true)
        }
    }

    fun getAppOpList(refresh: Boolean = false) {
        viewModelScope.launch {
            val appOpList = appOpRepo.getAppOpList(uid, pkgName, refresh)
            val opUiStates = appOpList.map { OpUiState.create(it) }
            _appOpsUiState.emit(_appOpsUiState.value.copy(ops = opUiStates))
        }
    }

    fun setMode(op: Int, uid: Int, pkgName: String, mode: Int) {
        viewModelScope.launch {
            iServerManager.appOpsManagerHidden.setMode(op, uid, pkgName, mode)
            getAppOpList(true)
        }
    }

    fun setUidMode(op: Int, uid: Int, mode: Int) {
        viewModelScope.launch {
            iServerManager.appOpsManagerHidden.setUidMode(op, uid, mode)
            getAppOpList(true)
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideViewModelFactory(
            assistedFactory: AssistedFactory,
            pkgInfo: PackageInfo
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return assistedFactory.create(pkgInfo) as T
                }
            }
        }
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(pkgInfo: PackageInfo): AppOpsViewModel
    }
}