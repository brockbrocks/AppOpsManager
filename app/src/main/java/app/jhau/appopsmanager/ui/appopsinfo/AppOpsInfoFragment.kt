package app.jhau.appopsmanager.ui.appopsinfo

import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.appopsinfo.AppOpsInfoActivity.Companion.PACKAGE_INFO
import app.jhau.appopsmanager.ui.view.IPreference
import app.jhau.framework.appops.OpEntry
import app.jhau.framework.appops.PackageOps
import app.jhau.server.IServerManager

class AppOpsInfoFragment : PreferenceFragmentCompat() {
    private lateinit var packageInfo: PackageInfo
    private val TAG = "tttt"
    private lateinit var pkgOps: List<PackageOps>

    override fun onCreate(savedInstanceState: Bundle?) {
        packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PACKAGE_INFO, PackageInfo::class.java)!!
        } else {
            arguments?.getParcelable(PACKAGE_INFO)!!
        }
        val uid = packageInfo.applicationInfo.uid
        val pkgName = packageInfo.packageName
        val iAppOpsManagerHidden =
            IServerManager.getIAppOpsManagerHidden(requireActivity().application)
        pkgOps = iAppOpsManagerHidden.getOpsForPackage(uid, pkgName, null)
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.appops_info, rootKey)
        if (pkgOps.isNotEmpty()) {
            for (opEntry in pkgOps.first().ops) {
                val preference = resolvePreference(opEntry)
                preferenceScreen.addPreference(preference)
            }
        }
    }

    private fun resolvePreference(opEntry: OpEntry): Preference {
        val iAppOpsManagerHidden =
            IServerManager.getIAppOpsManagerHidden(requireActivity().application)
        val op = opEntry.op
        val opStr = iAppOpsManagerHidden.opToName(op)
        val mode = opEntry.mode
        val preference = IPreference(requireContext())
        preference.apply {
            isIconSpaceReserved = false
            title = "$opStr(${op})"
            summary = iAppOpsManagerHidden.modeToName(mode)
        }
        return preference
    }
}