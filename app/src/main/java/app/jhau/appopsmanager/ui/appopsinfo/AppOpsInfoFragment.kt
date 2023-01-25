package app.jhau.appopsmanager.ui.appopsinfo

import android.os.Build
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.view.IPreference
import app.jhau.framework.AppOpsManagerApi
import app.jhau.framework.appops.AppOps

class AppOpsInfoFragment : PreferenceFragmentCompat() {
    private var packageOps: Array<AppOps.PkgOps>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        packageOps = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArray(PACKAGE_OPS, AppOps.PkgOps::class.java)
        } else {
            arguments?.getParcelableArray(PACKAGE_OPS) as Array<AppOps.PkgOps>?
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.appops_info, rootKey)
        packageOps?.let {
            if (it.isEmpty()) return
            for (opEntry in it.first().ops) {
                val preference = resolvePreference(opEntry)
                preferenceScreen.addPreference(preference)
            }
        }
    }

    private fun resolvePreference(opEntry: AppOps.OpEntry): Preference {
        val op = opEntry.op
        val opStr = AppOpsManagerApi.opToName(op)
        val mode = opEntry.mode
        val preference = IPreference(requireContext())
        preference.apply {
            isIconSpaceReserved = false
            title = "$opStr(${op})"
            summary = AppOpsManagerApi.MODE_NAMES[mode]
        }
        return preference
    }

    companion object {
        const val PACKAGE_OPS = "packageOps"
    }
}