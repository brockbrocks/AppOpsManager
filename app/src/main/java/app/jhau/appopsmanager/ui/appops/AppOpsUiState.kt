package app.jhau.appopsmanager.ui.appops

import app.jhau.appopsmanager.data.AppOp

data class AppOpsUiState(
    val ops: List<OpUiState> = emptyList()
)

data class OpUiState(
    val uidMode: Boolean,
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
        fun create(appOp: AppOp) = OpUiState(
            uidMode = appOp.uidMode,
            op = appOp.op,
            opName = appOp.opName,
            opStr = appOp.opStr,
            mode = appOp.mode,
            modeStr = appOp.modeStr,
            lastAccessTime = appOp.lastAccessTime,
            lastAccessForegroundTime = appOp.lastAccessForegroundTime,
            lastAccessBackgroundTime = appOp.lastAccessBackgroundTime,
            lastRejectTime = appOp.lastRejectTime,
            lastRejectForegroundTime = appOp.lastRejectForegroundTime,
            lastRejectBackgroundTime = appOp.lastRejectBackgroundTime,
            lastDuration = appOp.lastDuration,
            lastForegroundDuration = appOp.lastForegroundDuration,
            lastBackgroundDuration = appOp.lastBackgroundDuration,
            proxyUid = appOp.proxyUid,
            proxyPackageName = appOp.proxyPackageName,
        )
    }
}