package app.jhau.appopsmanager.ui.appopsinfo

import android.content.pm.PackageInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import app.jhau.server.IServerManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppOpsInfoViewModel @AssistedInject constructor(
    @Assisted pkgInfo: PackageInfo,
    private val iServerManager: IServerManager
) : ViewModel() {

    private val _appOpsInfoUiState: MutableStateFlow<AppOpsInfoUiState>
    val pkgName: String
    val uid: Int
    val modeNames: Array<String>

    init {
        val iAppOpsManager = iServerManager.appOpsManagerHidden
        pkgName = pkgInfo.packageName
        uid = pkgInfo.applicationInfo.uid
        modeNames = iAppOpsManager.modeNames
        _appOpsInfoUiState = MutableStateFlow(AppOpsInfoUiState())
        viewModelScope.launch { fetchPkgOps() }
    }

    val appOpsInfoUiState: StateFlow<AppOpsInfoUiState> = _appOpsInfoUiState.asStateFlow()
    fun setMode(op: Int, uid: Int, pkgName: String, mode: Int) = viewModelScope.launch {
        iServerManager.appOpsManagerHidden.setMode(op, uid, pkgName, mode)
        fetchPkgOps()
    }

    suspend fun fetchPkgOps() = withContext(Dispatchers.IO) {
        val iAppOpsManager = iServerManager.appOpsManagerHidden
        val pkgOps = iAppOpsManager.getOpsForPackage(uid, pkgName, null)
        val ops = if (!pkgOps.isNullOrEmpty()) {
            pkgOps.first().ops.map {
                OpUiState(
                    op = it.op,
                    opStr = iAppOpsManager.opToName(it.op),
                    mode = it.mode,
                    modeStr = iAppOpsManager.modeToName(it.mode)
                )
            }

        } else emptyList()
        _appOpsInfoUiState.emit(_appOpsInfoUiState.value.copy(ops = ops))
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
        fun create(pkgInfo: PackageInfo): AppOpsInfoViewModel
    }
}