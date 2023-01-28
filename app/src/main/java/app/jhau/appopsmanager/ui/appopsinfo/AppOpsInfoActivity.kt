package app.jhau.appopsmanager.ui.appopsinfo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ActivityAppOpsInfoBinding
import app.jhau.appopsmanager.ui.base.BaseBindingActivity

class AppOpsInfoActivity : BaseBindingActivity<ActivityAppOpsInfoBinding, AppOpsInfoViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val packageInfo: PackageInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(PACKAGE_INFO, PackageInfo::class.java)
        } else {
            intent.extras?.getParcelable(PACKAGE_INFO)
        }
        packageInfo?.let {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.appops_list, AppOpsInfoFragment().apply {
                    val bundle = Bundle()
                    bundle.putParcelable(PACKAGE_INFO, it)
                    arguments = bundle
                })
                .commit()
        }
    }

    companion object {
        const val PACKAGE_INFO = "packageInfo"

        fun start(context: Context, packageInfo: PackageInfo) {
            val intent = Intent(context, AppOpsInfoActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(PACKAGE_INFO, packageInfo)
            context.startActivity(intent)
        }
    }
}