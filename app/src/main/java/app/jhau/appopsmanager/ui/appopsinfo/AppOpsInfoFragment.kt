package app.jhau.appopsmanager.ui.appopsinfo

import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.view.Gravity
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.appopsinfo.AppOpsInfoActivity.Companion.PACKAGE_INFO
import app.jhau.appopsmanager.ui.view.IPreference
import app.jhau.framework.appops.AppOpsManagerHidden
import app.jhau.framework.appops.OpEntry
import app.jhau.framework.appops.PackageOps
import app.jhau.server.IServerManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AppOpsInfoFragment : PreferenceFragmentCompat() {
    private lateinit var pkgInfo: PackageInfo
    private lateinit var pkgOps: List<PackageOps>
    private lateinit var appOpsMgrHidden: AppOpsManagerHidden
    private lateinit var modeNames: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        pkgInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PACKAGE_INFO, PackageInfo::class.java)!!
        } else {
            arguments?.getParcelable(PACKAGE_INFO)!!
        }
        val uid = pkgInfo.applicationInfo.uid
        val pkgName = pkgInfo.packageName
        appOpsMgrHidden = IServerManager.getIAppOpsManagerHidden(requireActivity().application)
        modeNames = appOpsMgrHidden.modeNames
        pkgOps = appOpsMgrHidden.getOpsForPackage(uid, pkgName, null)
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.appops_info, rootKey)
        if (pkgOps.isNotEmpty()) {
            for (opEntry in pkgOps.first().ops) {
                val preference = resolvePreference(opEntry)
                preference.setOnPreferenceClickListener {
                    showChoiceModeDialog(opEntry)
                    true
                }
                preferenceScreen.addPreference(preference)
            }
        }
    }

    private fun showChoiceModeDialog(opEntry: OpEntry) {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("set mode")
            .setSingleChoiceItems(modeNames, opEntry.mode) { dialog, mode ->
                Toast.makeText(requireContext(), "op=${opEntry.op}, uid=${pkgInfo.applicationInfo.uid}, mode=${modeNames[mode]}", Toast.LENGTH_SHORT).show()
                try {
                    appOpsMgrHidden.setMode(opEntry.op, pkgInfo.applicationInfo.uid, pkgInfo.packageName, mode)
                } catch (e: RemoteException) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .create()
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()
    }

    private fun resolvePreference(opEntry: OpEntry): Preference {
        val op = opEntry.op
        val opStr = appOpsMgrHidden.opToName(op)
        val mode = opEntry.mode
        val preference = IPreference(requireContext())
        preference.apply {
            isIconSpaceReserved = false
            title = "$opStr(${op})"
            summary = appOpsMgrHidden.modeToName(mode)
        }
        return preference
    }
}