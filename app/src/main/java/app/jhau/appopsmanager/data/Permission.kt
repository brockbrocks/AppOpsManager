package app.jhau.appopsmanager.data

import android.content.pm.PermissionInfo
import app.jhau.framework.permission.PermissionInfoHidden
import app.jhau.framework.pms.PackageManagerHidden

data class Permission(
    val permInfo: PermissionInfo,
    val name: String,
    val granted: Boolean,
    val flags: Int,
) {
    val isChangeable: Boolean
        get() = isRuntime() && !isSystemFixed()

    fun isRuntime(): Boolean {
        return (permInfo.protectionLevel and PermissionInfo.PROTECTION_MASK_BASE) == PermissionInfo.PROTECTION_DANGEROUS
    }

    fun isAppOp(): Boolean {
        return (permInfo.protectionLevel and PermissionInfo.PROTECTION_FLAG_APPOP) != 0
    }

    fun isSystemFixed(): Boolean =
        (flags and PackageManagerHidden.FLAG_PERMISSION_SYSTEM_FIXED) != 0

    fun isPolicyFixed(): Boolean =
        (flags and PackageManagerHidden.FLAG_PERMISSION_POLICY_FIXED) != 0

    fun protectionStr(): String {
        val level = permInfo.protectionLevel
        val protStr = when (level and PermissionInfo.PROTECTION_MASK_BASE) {
            PermissionInfo.PROTECTION_DANGEROUS -> "dangerous"
            PermissionInfo.PROTECTION_NORMAL -> "normal"
            PermissionInfo.PROTECTION_SIGNATURE -> "signature"
            PermissionInfo.PROTECTION_SIGNATURE_OR_SYSTEM -> "signatureOrSystem"
            PermissionInfo.PROTECTION_INTERNAL -> "internal"
            else -> "????"
        }
        val protLevel = StringBuilder()
        if (level and PermissionInfo.PROTECTION_FLAG_PRIVILEGED != 0) {
            protLevel.append(", privileged")
        }
        if (level and PermissionInfo.PROTECTION_FLAG_DEVELOPMENT != 0) {
            protLevel.append(", development")
        }
        if (level and PermissionInfo.PROTECTION_FLAG_APPOP != 0) {
            protLevel.append(", appop")
        }
        if (level and PermissionInfo.PROTECTION_FLAG_PRE23 != 0) {
            protLevel.append(", pre23")
        }
        if (level and PermissionInfo.PROTECTION_FLAG_INSTALLER != 0) {
            protLevel.append(", installer")
        }
        if (level and PermissionInfo.PROTECTION_FLAG_VERIFIER != 0) {
            protLevel.append(", verifier")
        }
        if (level and PermissionInfo.PROTECTION_FLAG_PREINSTALLED != 0) {
            protLevel.append(", preinstalled")
        }
        if (level and PermissionInfo.PROTECTION_FLAG_SETUP != 0) {
            protLevel.append(", setup")
        }
        if (level and PermissionInfo.PROTECTION_FLAG_INSTANT != 0) {
            protLevel.append(", instant")
        }
        if (level and PermissionInfo.PROTECTION_FLAG_RUNTIME_ONLY != 0) {
            protLevel.append(", runtime")
        }
        if (level and PermissionInfoHidden.PROTECTION_FLAG_OEM != 0) {
            protLevel.append(", oem")
        }
        if (level and PermissionInfoHidden.PROTECTION_FLAG_VENDOR_PRIVILEGED != 0) {
            protLevel.append(", vendorPrivileged")
        }
        if (level and PermissionInfoHidden.PROTECTION_FLAG_SYSTEM_TEXT_CLASSIFIER != 0) {
            protLevel.append(", textClassifier")
        }
        if (level and PermissionInfoHidden.PROTECTION_FLAG_CONFIGURATOR != 0) {
            protLevel.append(", configurator")
        }
        if (level and PermissionInfoHidden.PROTECTION_FLAG_INCIDENT_REPORT_APPROVER != 0) {
            protLevel.append(", incidentReportApprover")
        }
        if (level and PermissionInfoHidden.PROTECTION_FLAG_APP_PREDICTOR != 0) {
            protLevel.append(", appPredictor")
        }
        if (level and PermissionInfoHidden.PROTECTION_FLAG_COMPANION != 0) {
            protLevel.append(", companion")
        }
        if (level and PermissionInfoHidden.PROTECTION_FLAG_RETAIL_DEMO != 0) {
            protLevel.append(", retailDemo")
        }
        if (level and PermissionInfoHidden.PROTECTION_FLAG_RECENTS != 0) {
            protLevel.append(", recents")
        }
        if (level and PermissionInfoHidden.PROTECTION_FLAG_ROLE != 0) {
            protLevel.append(", role")
        }
        if (level and PermissionInfoHidden.PROTECTION_FLAG_KNOWN_SIGNER != 0) {
            protLevel.append(", knownSigner")
        }

        return protStr + protLevel
    }

    fun toStringArray(): Array<String> {
        return arrayOf(
            "permName: $name",
            "granted: $granted",
            "flags: $flags",
            "isChangeable: $isChangeable",
            "isRuntime: ${isRuntime()}",
            "isAppOp: ${isAppOp()}",
            "isSystemFixed: ${isSystemFixed()}",
            "isPolicyFixed: ${isPolicyFixed()}",
            "protectionStr: ${protectionStr()}",
        )
    }
}