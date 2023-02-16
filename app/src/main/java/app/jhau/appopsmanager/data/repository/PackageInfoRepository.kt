package app.jhau.appopsmanager.data.repository

import android.content.pm.PackageInfo
import android.os.Build
import androidx.annotation.RequiresApi
import app.jhau.server.IServerManager
import javax.inject.Inject

class PackageInfoRepository @Inject constructor(
    private val packageInfoDataSource: PackageInfoDataSource
) {
    fun fetchPackageInfoList(flags: Long): Array<PackageInfo> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageInfoDataSource.fetchPackageInfoList(flags)
        } else {
            packageInfoDataSource.fetchPackageInfoList(flags.toInt())
        }
    }
}

class PackageInfoDataSource @Inject constructor(
    private val iServerManager: IServerManager
) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun fetchPackageInfoList(flags: Long): Array<PackageInfo> {
        val userId = android.os.Process.myUid() / 100000
        iServerManager.packageManagerHidden.getInstalledPackagesApi33(flags, userId)?.let { return it.toTypedArray() }
        return emptyArray()
    }

    fun fetchPackageInfoList(flags: Int): Array<PackageInfo> {
        val userId = android.os.Process.myUid() / 100000
        iServerManager.packageManagerHidden.getInstalledPackages(flags, userId)?.let { return it.toTypedArray() }
        return emptyArray()
    }
}