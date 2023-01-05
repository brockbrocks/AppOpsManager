package app.jhau.appopsmanager.data.repository

import android.app.Application
import android.content.pm.ApplicationInfo
import app.jhau.server.AppOpsServerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApplicationInfoRepository @Inject constructor(private val applicationDataSource: ApplicationInfoDataSource) {
    suspend fun fetchInstalledApplications(): List<ApplicationInfo> {
        return withContext(Dispatchers.IO) {
            applicationDataSource.fetchInstalledApplications()
        }
    }
}

class ApplicationInfoDataSource @Inject constructor(private val application: Application) {
    fun fetchInstalledApplications(): List<ApplicationInfo> {
        return AppOpsServerManager.getInstalledApplications(application).toList()
    }
}