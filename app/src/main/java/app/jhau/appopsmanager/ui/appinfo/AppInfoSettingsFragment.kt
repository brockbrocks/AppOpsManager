package app.jhau.appopsmanager.ui.appinfo

import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.appopsinfo.AppOpsInfoActivity

class AppInfoSettingsFragment : PreferenceFragmentCompat() {
    private lateinit var pkgInfo: PackageInfo
    private val viewModel: AppInfoViewModel by viewModels({ requireActivity() })
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
//                    val permIntent = Intent("android.intent.action.MANAGE_APP_PERMISSIONS")
//                    permIntent.putExtra(Intent.EXTRA_PACKAGE_NAME, "android")
//                    requireActivity().startActivity(permIntent)
                    viewModel.startPermissionControllerByADB(pkgInfo)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
                //AppPermsInfoActivity.start(requireContext(), pkgInfo)
            }
            "appops_settings" -> {
                AppOpsInfoActivity.start(requireContext(), pkgInfo)
            }
            else -> {}
        }
        return super.onPreferenceTreeClick(preference)
    }

    companion object {
        const val PACKAGE_INFO = "packageInfo"
    }
}