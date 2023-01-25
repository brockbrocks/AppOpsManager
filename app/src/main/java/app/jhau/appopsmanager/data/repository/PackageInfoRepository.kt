package app.jhau.appopsmanager.data.repository

import android.app.Application
import android.content.pm.PackageInfo
import app.jhau.server.IServerManager
import javax.inject.Inject

class PackageInfoRepository @Inject constructor(
    private val packageInfoDataSource: PackageInfoDataSource
) {
    fun fetchPackageInfoList(): Array<PackageInfo> {
        return packageInfoDataSource.fetchPackageInfoList()
    }
}

class PackageInfoDataSource @Inject constructor(private val application: Application) {
    fun fetchPackageInfoList(): Array<PackageInfo> {
        return IServerManager.getInstalledPackageInfoList(application).toTypedArray()
    }
}