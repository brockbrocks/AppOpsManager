package app.jhau.appopsmanager.data.repository

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.NameNotFoundException
import android.content.pm.PermissionInfo
import app.jhau.server.IServerManager
import javax.inject.Inject

class PermissionInfoRepository @Inject constructor(
    private val permInfoDateSource: PermissionInfoDateSource
) {
    private var perms: MutableMap<String, PermissionInfo?> = linkedMapOf()
    private var grantedFlags: MutableMap<String, Int> = mutableMapOf()

    fun fetchPermissionInfoList(
        permNames: Array<String>,
        permFlags: IntArray,
        update: Boolean = false
    ): List<Pair<String, PermissionInfo?>> {
        if (!update) {
            return perms.toList()
        }
        perms.clear()
        grantedFlags.clear()
        val ret = mutableListOf<Pair<String, PermissionInfo?>>()
        for (i in permNames.indices) {
            val permName = permNames[i]
            val permInfo = permInfoDateSource.getPermissionInfo(permName)
            ret.add(Pair(permName, permInfo))
            //cached
            perms[permName] = permInfoDateSource.getPermissionInfo(permName)
            grantedFlags[permName] = permFlags[i]
        }
        return ret
    }

    fun isGranted(permName: String): Boolean {
        val flag = grantedFlags[permName] ?: return false
        return (flag and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0
    }

    fun setPermissionGranted(pkgName: String, permName: String, granted: Boolean) {
        if (granted) {
            permInfoDateSource.grantRuntimePermission(pkgName, permName, userId())
        } else {
//            permInfoDateSource.revokeRuntimePermission(pkgName, permName, userId())
        }
    }

    private fun userId() = android.os.Process.myUid() / 100000
}

class PermissionInfoDateSource @Inject constructor(
    private val app: Application,
    private val iServerManager: IServerManager
) {
    fun getPermissionInfo(permName: String): PermissionInfo? {
        return try {
            app.packageManager.getPermissionInfo(permName, 0)
        } catch (e: NameNotFoundException) {
            null
        }
    }

    fun grantRuntimePermission(pkgName: String, permName: String, userId: Int) {
        iServerManager.packageManagerHidden.grantRuntimePermission(pkgName, permName, userId)
    }

    fun revokeRuntimePermission(pkgName: String, permName: String, userId: Int) {
        iServerManager.packageManagerHidden.revokeRuntimePermission(pkgName, permName, userId)
    }
}