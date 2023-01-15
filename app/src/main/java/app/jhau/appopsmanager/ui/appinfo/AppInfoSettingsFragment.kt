package app.jhau.appopsmanager.ui.appinfo

import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.appopsinfo.AppOpsInfoActivity

class AppInfoSettingsFragment : PreferenceFragmentCompat() {

    private var packageInfo: PackageInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PACKAGEINFO, PackageInfo::class.java)
        } else {
            arguments?.getParcelable(PACKAGEINFO)
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_info_settings, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "appops_settings" -> {
                packageInfo?.let {
                    AppOpsInfoActivity.start(requireContext(), it)
                }
                return true
            }
            else -> {}
        }
        return super.onPreferenceTreeClick(preference)
    }

    companion object {
        const val PACKAGEINFO = "packageInfo"
    }
}