package app.jhau.appopsmanager.data.repository

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.content.pm.PermissionInfo
import android.os.Build
import app.jhau.appopsmanager.data.Permission
import app.jhau.appopsmanager.util.userId
import app.jhau.server.IServerManager
import javax.inject.Inject

private typealias PackagePermissions = MutableMap<String, Permission>

//
class PermissionRepository @Inject constructor(
    private val pkgInfoRepo: PackageInfoRepository,
    private val permDataSource: PermissionDataSource,
    private val permInfoDataSource: PermissionInfoDataSource,
    private val iSvrMgr: IServerManager
) {
    private var permsStore: MutableMap<String, PackagePermissions> = linkedMapOf()

    suspend fun getPackagePermissions(pkgName: String, refresh: Boolean = false): List<Permission> {
        if (refresh) {
            val newPkgPermsMap = linkedMapOf<String, Permission>()
            permsStore[pkgName] = newPkgPermsMap
            val pkgInfo = pkgInfoRepo.getPackageInfo(pkgName, refresh = true)
            if (pkgInfo != null && !pkgInfo.requestedPermissions.isNullOrEmpty()) {
                for (i in pkgInfo.requestedPermissions.indices) {
                    val requestedPermName = pkgInfo.requestedPermissions[i]
                    val permInfo =
                        permInfoDataSource.getPermissionInfo(requestedPermName) ?: continue
                    val requestedPermFlag = pkgInfo.requestedPermissionsFlags[i]
                    val flags = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        iSvrMgr.permissionManagerHidden.getPermissionFlags(
                            requestedPermName,
                            pkgName,
                            userId
                        )
                    } else {
                        iSvrMgr.permissionManagerHidden.getPermissionFlagsApi33(
                            pkgName,
                            requestedPermName,
                            userId
                        )
                    }
                    newPkgPermsMap[requestedPermName] = permDataSource.createPermission(
                        permInfo,
                        requestedPermName,
                        requestedPermFlag,
                        flags
                    )
                }
            }
        }
        val pkgPermsMap = permsStore[pkgName] ?: linkedMapOf()
        val pkgPerms: List<Permission> = pkgPermsMap.values.toList()
        return pkgPerms
    }

    fun grantPermission(pkgName: String, permName: String) {
        try {
            val pkgPerms = permsStore[pkgName] ?: mutableMapOf()
            val perm = pkgPerms[permName] ?: return
            iSvrMgr.packageManagerHidden.grantRuntimePermission(pkgName, permName, userId)
            pkgPerms[permName] = perm.copy(granted = true)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun revokePermission(pkgName: String, permName: String) {
        try {
            val pkgPerms = permsStore[pkgName] ?: mutableMapOf()
            val perm = pkgPerms[permName] ?: return

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                iSvrMgr.packageManagerHidden.revokeRuntimePermission(pkgName, permName, userId)
            } else {
                iSvrMgr.permissionManagerHidden.revokeRuntimePermission(
                    pkgName,
                    permName,
                    userId,
                    ""
                )
            }
            pkgPerms[permName] = perm.copy(granted = false)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

class PermissionDataSource @Inject constructor() {
    fun createPermission(
        permInfo: PermissionInfo,
        permName: String,
        requestedPermsFlag: Int,
        flags: Int
    ): Permission {
        val isGranted = (requestedPermsFlag and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0
        return Permission(permInfo, permName, isGranted, flags)
    }
}


class PermissionInfoDataSource @Inject constructor(
    private val app: Application
) {
    fun getPermissionInfo(permName: String, flags: Int = 0): PermissionInfo? {
        return try {
            app.packageManager.getPermissionInfo(permName, flags)
        } catch (e: NameNotFoundException) {
            null
        }
    }
}