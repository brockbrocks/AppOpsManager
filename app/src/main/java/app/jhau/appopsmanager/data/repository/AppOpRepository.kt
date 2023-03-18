package app.jhau.appopsmanager.data.repository

import android.os.Build
import app.jhau.appopsmanager.data.AppOp
import app.jhau.framework.appops.PackageOps
import app.jhau.server.IServerManager
import javax.inject.Inject

class AppOpRepository @Inject constructor(
    private val pkgOpsDataSource: PackageOpsDataSource,
    private val iSvrMgr: IServerManager
) {
    private var appOpStore: MutableMap<String, List<AppOp>> = mutableMapOf()

    fun getAppOpList(uid: Int, pkgName: String, refresh: Boolean = false): List<AppOp> {
        if (refresh) {
            appOpStore.clear()
            val pkgOps = pkgOpsDataSource.getPackageOpsForPackage(uid, pkgName, null)
            val opNames = iSvrMgr.appOpsManagerHidden.opNames
            val modeNames = iSvrMgr.appOpsManagerHidden.modeNames
            if (pkgOps.isNotEmpty()) {
                appOpStore[pkgName] = mutableListOf<AppOp>().apply {
                    val uidAppOpMap = getUidAppOpList(uid, pkgName, opNames, modeNames)
                    pkgOps.first().ops.forEach {opEntry ->
                        add(AppOp.create(uid, false, pkgName, opNames, modeNames, opEntry))
                        uidAppOpMap[opEntry.op]?.let { add(it) }
                    }
                }
            } else {
                appOpStore[pkgName] = emptyList()
            }
        }
        return appOpStore[pkgName] ?: emptyList()
    }

    private fun getUidAppOpList(uid: Int, pkgName: String, opNames: Array<String>, modeNames: Array<String>): Map<Int, AppOp> {
        val pkgOps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            iSvrMgr.appOpsManagerHidden.getUidOps(uid, null)?: emptyList()
        } else emptyList()
        if (pkgOps.isNotEmpty()) {
            val uidAppOpMap = mutableMapOf<Int, AppOp>()
            pkgOps.first().ops.forEach {
                uidAppOpMap[it.op] = AppOp.create(uid, true, pkgName, opNames, modeNames, it)
            }
            return uidAppOpMap
        }
        return emptyMap()
    }
}

class PackageOpsDataSource @Inject constructor(
    private val iSvrMgr: IServerManager
) {
    fun getPackageOpsForPackage(uid: Int, pkgName: String, ops: IntArray?): List<PackageOps> {
        return iSvrMgr.appOpsManagerHidden.getOpsForPackage(uid, pkgName, ops)
    }
}