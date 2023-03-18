package app.jhau.appopsmanager.data

import android.os.Build
import app.jhau.framework.appops.OpEntry
import kotlin.math.max

data class AppOp(
    val uid: Int,
    val pkgName: String,
    val op: Int,
    val opName: String = "",
    val opStr: String = "",
    val mode: Int,
    val modeStr: String = "",
    val lastAccessTime: Long = -1,
    val lastAccessForegroundTime: Long = -1,
    val lastAccessBackgroundTime: Long = -1,
    val lastRejectTime: Long = -1,
    val lastRejectForegroundTime: Long = -1,
    val lastRejectBackgroundTime: Long = -1,
    val lastDuration: Long = -1,
    val lastForegroundDuration: Long = -1,
    val lastBackgroundDuration: Long = -1,
    val proxyUid: Int = -1,
    val proxyPackageName: String = "",
) {
    companion object {
        fun create(
            uid: Int,
            pkgName: String,
            opNames: Array<String>,
            modeNames: Array<String>,
            opEntry: OpEntry
        ): AppOp {
            //op
            val op = opEntry.op
            //opName
            val opName = if (op < opNames.size) opNames[op] else "OP_$op"
            //lastAccessTime
            val lastAccessTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                max(opEntry.lastAccessForegroundTime, opEntry.lastAccessBackgroundTime)
            } else {
                opEntry.time
            }
            //lastRejectTime
            val lastRejectTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                max(opEntry.lastRejectForegroundTime, opEntry.lastRejectBackgroundTime)
            } else {
                opEntry.rejectTime
            }
            //lastDuration
            val lastDuration = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                opEntry.lastDuration
            } else {
                opEntry.duration
            }
            return AppOp(
                uid = uid,
                pkgName = pkgName,
                op = opEntry.op,
                opName = opName,
                mode = opEntry.mode,
                modeStr = modeNames[opEntry.mode],
                lastAccessTime = lastAccessTime,
                lastAccessForegroundTime = opEntry.lastAccessForegroundTime,
                lastAccessBackgroundTime = opEntry.lastAccessBackgroundTime,
                lastRejectTime = lastRejectTime,
                lastRejectForegroundTime = opEntry.lastRejectForegroundTime,
                lastRejectBackgroundTime = opEntry.lastRejectBackgroundTime,
                lastDuration = lastDuration,
                lastForegroundDuration = opEntry.lastForegroundDuration,
                lastBackgroundDuration = opEntry.lastBackgroundDuration,
                proxyUid = opEntry.proxyUid,
                proxyPackageName = opEntry.proxyPackageName
            )
        }
    }
}