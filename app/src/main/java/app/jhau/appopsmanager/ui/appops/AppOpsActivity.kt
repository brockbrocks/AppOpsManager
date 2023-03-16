package app.jhau.appopsmanager.ui.appops

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ActivityAppOpsBinding
import app.jhau.appopsmanager.ui.base.BaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppOpsActivity : BaseActivity<ActivityAppOpsBinding, AppOpsViewModel>() {
    @Inject
    lateinit var factory: AppOpsViewModel.AssistedFactory

    override lateinit var viewModel: AppOpsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pkgInfo: PackageInfo? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelable(PACKAGE_INFO, PackageInfo::class.java)
        } else {
            intent.extras?.getParcelable(PACKAGE_INFO)
        }
        if (pkgInfo == null) return
        supportActionBar?.apply {
            title = packageManager.getApplicationLabel(pkgInfo.applicationInfo)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        }
        viewModel = ViewModelProvider(
            this,
            AppOpsViewModel.provideViewModelFactory(factory, pkgInfo)
        )[AppOpsViewModel::class.java]

        binding.appopsList.apply {
            adapter = AppOpsAdapter { opUiState ->
                showChoiceModeDialog(opUiState)
            }
            val linearManager =
                LinearLayoutManager(this@AppOpsActivity, LinearLayoutManager.VERTICAL, false)
            layoutManager = linearManager
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.appOpsInfoUiState.collect {
                    (binding.appopsList.adapter as AppOpsAdapter).submitList(it.ops)
                }
            }
        }
    }

    private fun showChoiceModeDialog(opUiState: OpUiState) {
        val modeNames = viewModel.modeNames
        val op = opUiState.op
        val curMode = opUiState.mode
        val dialog = MaterialAlertDialogBuilder(this)
            .setSingleChoiceItems(modeNames, curMode) { dialog, mode ->
                viewModel.setMode(op, viewModel.uid, viewModel.pkgName, mode)
                dialog.dismiss()
            }.create()
        dialog.window!!.setGravity(Gravity.BOTTOM)
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val PACKAGE_INFO = "packageInfo"

        fun start(context: Context, pkgInfo: PackageInfo) {
            val intent = Intent(context, AppOpsActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(PACKAGE_INFO, pkgInfo)
            context.startActivity(intent)
        }
    }
}