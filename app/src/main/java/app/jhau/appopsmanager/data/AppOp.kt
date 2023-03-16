package app.jhau.appopsmanager.data

import app.jhau.framework.appops.OpEntry

data class AppOp(
    val uid: Int,
    val pkgName: String,
    val op: Int,
    val mode: Int
) {
    companion object {
        fun parseAppOp(uid: Int, pkgName: String, opEntry: OpEntry): AppOp {
            return AppOp(
                uid,
                pkgName,
                opEntry.op,
                opEntry.mode
            )
        }
    }
}