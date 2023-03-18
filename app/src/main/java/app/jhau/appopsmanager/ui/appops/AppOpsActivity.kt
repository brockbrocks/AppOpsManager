package app.jhau.appopsmanager.ui.appops

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ActivityAppOpsBinding
import app.jhau.appopsmanager.databinding.DialogAppOpDetailBinding
import app.jhau.appopsmanager.ui.base.BaseActivity
import app.jhau.appopsmanager.util.dateStr
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
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
            adapter = AppOpsAdapter(
                this@AppOpsActivity::showChoiceModeDialog,
                this@AppOpsActivity::onOpLongClick
            )
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

    @SuppressLint("SetTextI18n")
    private fun onOpLongClick(opUiState: OpUiState): Boolean {
        val vb = DialogAppOpDetailBinding.inflate(layoutInflater)
        //op
        vb.op.text = "op: " + opUiState.opName
        //mode
        vb.mode.text = "mode: " + opUiState.modeStr
        //lastAccessTime
        if (opUiState.lastAccessTime > 0 && (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)) {
            vb.lastAccessTime.text = "lastAccessTime: ${opUiState.lastAccessTime.dateStr("yyyy/M/dd HH:mm:ss")}"
        } else {
            vb.lastAccessTime.visibility = View.GONE
        }
        //lastAccessForegroundTime
        if (opUiState.lastAccessForegroundTime > 0) {
            vb.lastAccessForegroundTime.text =
                "lastAccessForegroundTime: ${opUiState.lastAccessForegroundTime.dateStr("yyyy/M/dd HH:mm:ss")}"
        } else {
            vb.lastAccessForegroundTime.visibility = View.GONE
        }
        //lastAccessBackgroundTime
        if (opUiState.lastAccessBackgroundTime > 0) {
            vb.lastAccessBackgroundTime.text =
                "lastAccessBackgroundTime: ${opUiState.lastAccessBackgroundTime.dateStr("yyyy/M/dd HH:mm:ss")}"
        } else {
            vb.lastAccessBackgroundTime.visibility = View.GONE
        }
        //lastRejectTime
        if (opUiState.lastRejectTime > 0 && (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q)) {
            vb.lastRejectTime.text = "lastRejectTime: ${opUiState.lastRejectTime.dateStr("yyyy/M/dd HH:mm:ss")}"
        } else {
            vb.lastRejectTime.visibility = View.GONE
        }
        //lastRejectForegroundTime
        if (opUiState.lastRejectForegroundTime > 0) {
            vb.lastRejectForegroundTime.text =
                "lastRejectForegroundTime: ${opUiState.lastRejectForegroundTime.dateStr("yyyy/M/dd HH:mm:ss")}"
        } else {
            vb.lastRejectForegroundTime.visibility = View.GONE
        }
        //lastRejectBackgroundTime
        if (opUiState.lastRejectBackgroundTime > 0) {
            vb.lastRejectBackgroundTime.text =
                "lastRejectBackgroundTime: ${opUiState.lastRejectBackgroundTime.dateStr("yyyy/M/dd HH:mm:ss")}"
        } else {
            vb.lastRejectBackgroundTime.visibility = View.GONE
        }
        //lastDuration
        if (opUiState.lastDuration > 0) {
            vb.lastDuration.text = "lastDuration: ${opUiState.lastDuration}"
        } else {
            vb.lastDuration.visibility = View.GONE
        }
        //proxyUid
        if (opUiState.proxyUid >= 0) {
            vb.proxyUid.text = "proxyUid: ${opUiState.proxyUid}"
        } else {
            vb.proxyUid.visibility = View.GONE
        }
        //proxyPackageName
        if (opUiState.proxyPackageName.isNotBlank()) {
            vb.proxyPackageName.text = "proxyPackage: ${opUiState.proxyPackageName}"
        } else {
            vb.proxyPackageName.visibility = View.GONE
        }
        val dialog = MaterialAlertDialogBuilder(this).setView(vb.root).create()
        dialog.show()
        return true
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