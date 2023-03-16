package app.jhau.appopsmanager.ui.appsetting

import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.appops.AppOpsActivity
import app.jhau.appopsmanager.ui.appperms.AppPermsActivity
import app.jhau.appopsmanager.ui.appsetting.AppSettingActivity.Companion.PACKAGE_INFO

class AppSettingsFragment : PreferenceFragmentCompat() {
    private lateinit var pkgInfo: PackageInfo

    //private val viewModel: AppInfoViewModel by viewModels({ requireActivity() })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(PACKAGE_INFO, PackageInfo::class.java)?.also { pkgInfo = it }
        } else {
            (arguments?.getParcelable(PACKAGE_INFO) as? PackageInfo)?.also { pkgInfo = it }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.app_info_settings, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "permission_settings" -> {
                try {
                    AppPermsActivity.start(requireContext(), pkgInfo)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            "appops_settings" -> {
                AppOpsActivity.start(requireContext(), pkgInfo)
            }
            else -> {}
        }
        return super.onPreferenceTreeClick(preference)
    }
}