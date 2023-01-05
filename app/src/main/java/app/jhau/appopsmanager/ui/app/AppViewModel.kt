package app.jhau.appopsmanager.ui.app

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.jhau.appopsmanager.data.repository.ApplicationInfoRepository
import app.jhau.server.AppOpsServerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val application: Application,
    private val applicationInfoRepository: ApplicationInfoRepository
) : ViewModel() {

    private val _appUiState = MutableStateFlow(AppUiState())
    val appUiState = _appUiState.asStateFlow()

    init {
        viewModelScope.launch {
            val installedApplications = applicationInfoRepository.fetchInstalledApplications()
            val appItemUiStateList = installedApplications.map {
                AppItemUiState(
                    application.packageManager.getApplicationLabel(it).toString(),
                    it.packageName,
                    application.packageManager.getApplicationIcon(it)
                )
            }
            val oldValue = _appUiState.value
            _appUiState.emit(oldValue.copy(apps = appItemUiStateList))
        }
    }

}