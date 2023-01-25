package app.jhau.appopsmanager.data.repository

import android.app.Application
import app.jhau.framework.appops.AppOps
import app.jhau.server.IServerManager
import javax.inject.Inject

class PackageOpsRepository @Inject constructor(
    private val packageOpsDataSource: PackageOpsDataSource
) {
    fun fetchPackageOps(uid: Int, packageName: String): Array<AppOps.PkgOps> {
        return packageOpsDataSource.fetchPackageOps(uid, packageName).toTypedArray()
    }
}

class PackageOpsDataSource @Inject constructor(
    private val application: Application
) {
    fun fetchPackageOps(
        uid: Int,
        packageName: String
    ): List<AppOps.PkgOps> {
        return IServerManager.getOpsForPackage(application, uid, packageName)
    }
}