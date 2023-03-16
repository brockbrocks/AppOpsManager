package app.jhau.appopsmanager.data.repository

import app.jhau.appopsmanager.data.AppOp
import app.jhau.framework.appops.PackageOps
import app.jhau.server.IServerManager
import javax.inject.Inject

class AppOpRepository @Inject constructor(
    private val pkgOpsDataSource: PackageOpsDataSource
) {
    private var appOpStore: MutableMap<String, List<AppOp>> = mutableMapOf()

    fun getAppOpList(uid: Int, pkgName: String, refresh: Boolean = false): List<AppOp> {
        if (refresh) {
            val pkgOps = pkgOpsDataSource.getPackageOpsForPackage(uid, pkgName, null)
            if (pkgOps.isNotEmpty()) {
                appOpStore[pkgName] = pkgOps.first().ops.map { AppOp.parseAppOp(uid, pkgName, it) }
            } else {
                appOpStore[pkgName] = emptyList()
            }
        }
        return appOpStore[pkgName] ?: emptyList()
    }
}

class PackageOpsDataSource @Inject constructor(
    private val iSvrMgr: IServerManager
) {
    fun getPackageOpsForPackage(uid: Int, pkgName: String, ops: IntArray?): List<PackageOps> {
        return iSvrMgr.appOpsManagerHidden.getOpsForPackage(uid, pkgName, ops)
    }
}