package app.jhau.appopsmanager.ui.appperms

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ActivityAppPermsBinding
import app.jhau.appopsmanager.ui.appopsinfo.AppOpsInfoActivity
import app.jhau.appopsmanager.ui.base.BaseActivity
import app.jhau.server.IServerManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppPermsActivity : BaseActivity<ActivityAppPermsBinding, AppPermsViewModel>() {
    @Inject
    lateinit var factory: AppPermsViewModel.AssistedFactory
    override lateinit var viewModel: AppPermsViewModel

    @Inject
    lateinit var iServerManager: IServerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pkgInfo: PackageInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(AppOpsInfoActivity.PACKAGE_INFO, PackageInfo::class.java)
        } else {
            intent.extras?.getParcelable(AppOpsInfoActivity.PACKAGE_INFO)
        }
        if (pkgInfo == null) return
        supportActionBar?.apply {
            title = pkgInfo.applicationInfo.loadLabel(packageManager)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        viewModel = ViewModelProvider(
            this,
            AppPermsViewModel.provideFactory(factory, pkgInfo)
        )[AppPermsViewModel::class.java]
        setupAdapter()
        collectData()
    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.appPermInfoList.collect {
                    (binding.permsList.adapter as AppPermsAdapter).submitList(it)
                }
            }
        }
    }

    private fun setupAdapter() {
        binding.permsList.adapter = AppPermsAdapter(this::setPermission, this::checkGranted)
        binding.permsList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setPermission(permName: String, permChecked: Boolean) {
//        val pkgName = viewModel.pkgInfo.packageName
        viewModel.setPermission(permName, permChecked)
//        Toast.makeText(this, "${pkgName} \n${permChecked}", Toast.LENGTH_SHORT).show()
    }

    private fun checkGranted(permName: String): Boolean {
        return viewModel.checkGranted(permName)
    }

    companion object {
        const val PACKAGE_INFO = "packageInfo"

        fun start(context: Context, pkgInfo: PackageInfo) {
            val intent = Intent(context, AppPermsActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(PACKAGE_INFO, pkgInfo)
            context.startActivity(intent)
        }
    }
}