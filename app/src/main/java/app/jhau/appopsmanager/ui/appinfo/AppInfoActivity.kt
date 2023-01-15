package app.jhau.appopsmanager.ui.appinfo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ActivityAppDetailBinding
import app.jhau.appopsmanager.ui.base.BaseBindingActivity
import app.jhau.appopsmanager.ui.setting.SettingsFragment

class AppInfoActivity : BaseBindingActivity<ActivityAppDetailBinding, AppInfoViewModel>() {
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

        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(AppInfoSettingsFragment.PACKAGEINFO, PackageInfo::class.java)
        } else {
            intent.getParcelableExtra(AppInfoSettingsFragment.PACKAGEINFO)
        }

        packageInfo?.let {
            binding.appIcon.setImageDrawable(packageManager.getApplicationIcon(it.applicationInfo))
            binding.appName.text = packageManager.getApplicationLabel(it.applicationInfo)
            binding.applicationId.text = it.packageName
            binding.appVersion.text = it.versionName
            //
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.app_info_settings, AppInfoSettingsFragment().apply {
                    val bundle = Bundle()
                    bundle.putParcelable(AppInfoSettingsFragment.PACKAGEINFO, it)
                    arguments = bundle
                })
                .commit()
        }
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
            intent.putExtra(AppInfoSettingsFragment.PACKAGEINFO, packageInfo)
            context.startActivity(intent)
        }
    }
}