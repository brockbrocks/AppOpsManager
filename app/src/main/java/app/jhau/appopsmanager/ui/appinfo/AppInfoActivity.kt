package app.jhau.appopsmanager.ui.appinfo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ActivityAppDetailBinding
import app.jhau.appopsmanager.ui.base.DataBindingActivity

class AppInfoActivity : DataBindingActivity<ActivityAppDetailBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }

        val pkgInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(AppInfoSettingsFragment.PACKAGE_INFO, PackageInfo::class.java)
        } else {
            intent.getParcelableExtra(AppInfoSettingsFragment.PACKAGE_INFO)
        }
        if (pkgInfo == null) return
        binding.appIcon.setImageDrawable(packageManager.getApplicationIcon(pkgInfo.applicationInfo))
        binding.appName.text = packageManager.getApplicationLabel(pkgInfo.applicationInfo)
        binding.applicationId.text = pkgInfo.packageName
        binding.appVersion.text = pkgInfo.versionName
        //
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.app_info_settings, AppInfoSettingsFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(AppInfoSettingsFragment.PACKAGE_INFO, pkgInfo)
                arguments = bundle
            })
            .commit()
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
        fun start(context: Context, packageInfo: PackageInfo) {
            val intent = Intent(context, AppInfoActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(AppInfoSettingsFragment.PACKAGE_INFO, packageInfo)
            context.startActivity(intent)
        }
    }
}