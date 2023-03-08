package app.jhau.appopsmanager.data.repository

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import app.jhau.appopsmanager.util.userId
import app.jhau.server.IServerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PackageInfoRepository @Inject constructor(
    private val pkgInfoDataSource: PackageInfoDataSource,
    private val iSvrMgr: IServerManager
) {
    private var pkgInfoStore: MutableMap<String, PackageInfo> = linkedMapOf()

    suspend fun getPackageInfo(
        pkgName: String,
        flags: Int = PackageManager.GET_PERMISSIONS,
        refresh: Boolean = false
    ): PackageInfo? = withContext(Dispatchers.IO) {
        if (refresh) {
            if (pkgInfoStore[pkgName] != null) {
                pkgInfoStore[pkgName] = pkgInfoDataSource.getPackageInfo(pkgName, flags)
            } else {
                getPackageInfoList(flags, true)
            }
        }
        return@withContext pkgInfoStore[pkgName]
    }

    suspend fun getPackageInfoList(
        flags: Int = PackageManager.GET_PERMISSIONS,
        refresh: Boolean = false
    ): List<PackageInfo> = withContext(Dispatchers.IO) {
        if (refresh) {
            pkgInfoStore.clear()
            pkgInfoDataSource.getInstalledPackages(flags)
                .map { pkgInfoStore.put(it.packageName, it) }
        }
        return@withContext pkgInfoStore.values.toList()
    }

    fun setPackageEnable(pkgName: String, newState: Int): PackageInfo {
        iSvrMgr.packageManagerHidden.setApplicationEnabledSetting(pkgName, newState, 0, userId, "")
        val pkg = pkgInfoDataSource.getPackageInfo(pkgName)
        pkgInfoStore[pkgName] = pkg
        return pkg
    }
}

class PackageInfoDataSource @Inject constructor(
    private val app: Application
) {
    fun getInstalledPackages(flags: Int): List<PackageInfo> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            app.packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            app.packageManager.getInstalledPackages(flags)
        }
    }

    fun getPackageInfo(pkgName: String, flags: Int = PackageManager.GET_PERMISSIONS): PackageInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            app.packageManager.getPackageInfo(pkgName,PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            app.packageManager.getPackageInfo(pkgName, flags)
        }
    }
}