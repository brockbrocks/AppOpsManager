package app.jhau.appopsmanager.ui.apppermsinfo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ActivityAppPermsInfoBinding
import app.jhau.appopsmanager.ui.appopsinfo.AppOpsInfoActivity
import app.jhau.appopsmanager.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppPermsInfoActivity : BaseActivity<ActivityAppPermsInfoBinding, AppPermsInfoViewModel>() {
    @Inject
    lateinit var factory: AppPermsInfoViewModel.AssistedFactory
    override lateinit var viewModel: AppPermsInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pkgInfo: PackageInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(AppOpsInfoActivity.PACKAGE_INFO, PackageInfo::class.java)
        } else {
            intent.extras?.getParcelable(AppOpsInfoActivity.PACKAGE_INFO)
        }
        if (pkgInfo == null) return
        supportActionBar?.apply {
            title = packageManager.getApplicationLabel(pkgInfo.applicationInfo)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        //test
//        if (pkgInfo.packageName.equals("app.testapp")){
//            Log.i("tttt", "onCreate: pkgInfo.packageName.equals(\"app.testapp\")")
//            IServerManager.grantRuntimePermission(application, "app.testapp", Manifest.permission.ACCESS_COARSE_LOCATION, 0);
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val PACKAGE_INFO = "packageInfo"

        fun start(context: Context, pkgInfo: PackageInfo) {
            val intent = Intent(context, AppPermsInfoActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(PACKAGE_INFO, pkgInfo)
            context.startActivity(intent)
        }
    }
}