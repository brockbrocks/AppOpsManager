package app.jhau.appopsmanager.ui.appsetting

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ActivityAppSettingBinding
import app.jhau.appopsmanager.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppSettingActivity : BaseActivity<ActivityAppSettingBinding, AppSettingViewModel>() {
    @Inject
    lateinit var factory: AppSettingViewModel.AssistedFactory

    override lateinit var viewModel: AppSettingViewModel
    private lateinit var setEnableItem: MenuItem

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
            intent.getParcelableExtra(PACKAGE_INFO, PackageInfo::class.java)
        } else {
            intent.getParcelableExtra(PACKAGE_INFO)
        }
        if (pkgInfo == null) return
        viewModel = ViewModelProvider(
            this,
            AppSettingViewModel.provideViewModelFactory(factory, pkgInfo)
        )[AppSettingViewModel::class.java]
        //
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.app_info_settings, AppSettingsFragment().apply {
                val bundle = Bundle()
                bundle.putParcelable(PACKAGE_INFO, pkgInfo)
                arguments = bundle
            })
            .commit()
        //
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.pkgInfo.map {
                        //set app name
                        val appName = it.applicationInfo.loadLabel(packageManager)
                        val enable = it.applicationInfo.enabled
                        if (!enable) {
                            val disableStr = "(${getString(R.string.disabled)})"
                            val appNameStr = SpannableString("${appName}${disableStr}")
                            appNameStr.setSpan(
                                ForegroundColorSpan(getColor(R.color.blue_0085F8)),
                                appName.length,
                                appName.length + disableStr.length,
                                SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
                            )
                            binding.appName.text = appNameStr
                        } else binding.appName.text = appName
                        it
                    }.collect {
                        binding.appIcon.setImageDrawable(packageManager.getApplicationIcon(it.applicationInfo))
                        binding.applicationId.text = it.packageName
                        binding.appVersion.text = it.versionName
                    }
                }
                launch {
                    viewModel.onSetAppEnableSetting.collect {
                        if (it) {
                            setEnableItem.title = getString(R.string.disable_app)
                        } else {
                            setEnableItem.title = getString(R.string.enable_app)
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.setEnabled -> {
                viewModel.switchAppEnableState()
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app_settings, menu)
        setEnableItem = menu!!.findItem(R.id.setEnabled)
        if (viewModel.pkgInfo.value.applicationInfo.enabled) {
            setEnableItem.title = getString(R.string.disable_app)
        } else {
            setEnableItem.title = getString(R.string.enable_app)
        }
        return super.onCreateOptionsMenu(menu)
    }

    companion object {

        const val PACKAGE_INFO = "packageInfo"
        fun start(context: Context, pkgInfo: PackageInfo) {
            val intent = Intent(context, AppSettingActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(PACKAGE_INFO, pkgInfo)
            context.startActivity(intent)
        }
    }
}