package app.jhau.appopsmanager.data.repository

import android.app.Application
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.PackageInfoFlags
import android.content.pm.PackageManager.ResolveInfoFlags
import android.content.pm.ResolveInfo
import android.os.Build
import androidx.annotation.RequiresApi
import app.jhau.server.IServerManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PackageInfoRepository @Inject constructor(
    private val packageInfoDataSource: PackageInfoDataSource,
    private val app: Application
) {
    private fun sortLogic(pkg1: PackageInfo, pkg2: PackageInfo, sortType: SortType): Int {
        return when (sortType) {
            SortType.NAME -> {
                val pkg1Name = pkg1.applicationInfo.loadLabel(app.packageManager).toString()
                val pkg2Name = pkg2.applicationInfo.loadLabel(app.packageManager).toString()
                pkg1Name.compareTo(pkg2Name)
            }
            SortType.UID -> pkg1.applicationInfo.uid - pkg2.applicationInfo.uid
        }
    }

    private fun filterLogic(
        pkgInfo: PackageInfo,
        filterTypeSet: Set<FilterType>
    ): Boolean {
        var filterOut = false
        val isSystemApp = (pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0

        if (filterTypeSet.contains(FilterType.USER_APP)){
            filterOut = filterOut || !isSystemApp
        }

        if (filterTypeSet.contains(FilterType.SYSTEM_APP)) {
            filterOut = filterOut || isSystemApp
        }

        if (filterTypeSet.contains(FilterType.DISABLED)) {
            filterOut = filterOut || !pkgInfo.applicationInfo.enabled
        }

        return filterOut
    }

    suspend fun findPermissionControllerInfo(): List<ResolveInfo> = withContext(Dispatchers.IO) {
        return@withContext packageInfoDataSource.findPermissionController(0)
    }

    suspend fun fetchPackageInfoList(
        flags: Int,
        sortType: SortType,
        filterTypes: Set<FilterType>
    ): List<PackageInfo> = withContext(Dispatchers.IO) {
        var ret = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageInfoDataSource.fetchPackageInfoListApi33(flags.toLong())
        } else {
            packageInfoDataSource.fetchPackageInfoList(flags)
        }
        ret = ret.filter { filterLogic(it, filterTypes) }.sortedWith { o1, o2 -> sortLogic(o1, o2, sortType) }
        return@withContext ret
    }

    fun fetchPackageInfo(pkgName: String, flags: Long): PackageInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageInfoDataSource.fetchPackageInfoApi33(pkgName, flags, getUserId())
        } else {
            packageInfoDataSource.fetchPackageInfo(pkgName, flags.toInt(), getUserId())
        }
    }

    fun setPackageEnable(pkgName: String, newState: Int): PackageInfo {
        packageInfoDataSource.setPackageEnable(pkgName, newState, 0, getUserId(), pkgName)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageInfoDataSource.fetchPackageInfoApi33(pkgName, 0L, getUserId())
        } else {
            packageInfoDataSource.fetchPackageInfo(pkgName, 0, getUserId())
        }
    }

    private fun getUserId() = android.os.Process.myUid() / 100000

    enum class SortType {
        NAME, UID
    }

    enum class FilterType {
        USER_APP, SYSTEM_APP, DISABLED
    }
}

class PackageInfoDataSource @Inject constructor(
    private val iServerManager: IServerManager,
    private val application: Application
) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun fetchPackageInfoListApi33(flags: Long): List<PackageInfo> {
        return application.packageManager.getInstalledPackages(PackageInfoFlags.of(flags))
    }

    fun fetchPackageInfoList(flags: Int): List<PackageInfo> {
        return application.packageManager.getInstalledPackages(flags)
    }

    fun fetchPackageInfo(pkgName: String, flags: Int, userId: Int): PackageInfo {
        return iServerManager.packageManagerHidden.getPackageInfo(pkgName, flags, userId)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun fetchPackageInfoApi33(pkgName: String, flags: Long, userId: Int): PackageInfo {
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

    fun findPermissionController(flags: Int): List<ResolveInfo> {
        val intent = Intent("android.intent.action.MANAGE_APP_PERMISSIONS")
        val resolveInfoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            application.packageManager.queryIntentActivities(
                intent,
                ResolveInfoFlags.of(flags.toLong())
            )
        } else {
            application.packageManager.queryIntentActivities(intent, flags)
        }
        return resolveInfoList
    }
}