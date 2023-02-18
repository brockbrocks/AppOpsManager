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
            packageInfoDataSource.fetchPackageInfoList(flags, getUserId())
        } else {
            packageInfoDataSource.fetchPackageInfoList(flags.toInt(), getUserId())
        }
    }

    fun fetchPackageInfo(pkgName: String, flags: Long): PackageInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageInfoDataSource.fetchPackageInfo(pkgName, flags, getUserId())
        } else {
            packageInfoDataSource.fetchPackageInfo(pkgName, flags.toInt(), getUserId())
        }
    }

    fun setPackageEnable(pkgName: String, newState: Int): PackageInfo {
        packageInfoDataSource.setPackageEnable(pkgName, newState, 0, getUserId(), pkgName)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageInfoDataSource.fetchPackageInfo(pkgName, 0L, getUserId())
        } else {
            packageInfoDataSource.fetchPackageInfo(pkgName, 0, getUserId())
        }
    }

    private fun getUserId() = android.os.Process.myUid() / 100000
}

class PackageInfoDataSource @Inject constructor(
    private val iServerManager: IServerManager
) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun fetchPackageInfoList(flags: Long, userId: Int): Array<PackageInfo> {
        iServerManager.packageManagerHidden.getInstalledPackagesApi33(flags, userId)
            ?.let { return it.toTypedArray() }
        return emptyArray()
    }

    fun fetchPackageInfoList(flags: Int, userId: Int): Array<PackageInfo> {
        iServerManager.packageManagerHidden.getInstalledPackages(flags, userId)
            ?.let { return it.toTypedArray() }
        return emptyArray()
    }

    fun fetchPackageInfo(pkgName: String, flags: Int, userId: Int): PackageInfo {
        return iServerManager.packageManagerHidden.getPackageInfo(pkgName, flags, userId)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun fetchPackageInfo(pkgName: String, flags: Long, userId: Int): PackageInfo {
        return iServerManager.packageManagerHidden.getPackageInfoApi33(pkgName, flags, userId)
    }

    fun setPackageEnable(
        pkgName: String,
        newState: Int,
        flags: Int,
        userId: Int,
        callingPackage: String
    ) {
        iServerManager.packageManagerHidden.setApplicationEnabledSetting(
            pkgName,
            newState,
            flags,
            userId,
            callingPackage
        )
    }
}