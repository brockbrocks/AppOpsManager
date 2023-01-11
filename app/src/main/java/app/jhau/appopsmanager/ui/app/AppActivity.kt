package app.jhau.appopsmanager.ui.app

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ActivityAppBinding
import app.jhau.appopsmanager.ui.base.BaseBindingActivity
import app.jhau.appopsmanager.ui.setting.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppActivity : BaseBindingActivity<ActivityAppBinding, AppViewModel>() {
    private val TAG = "AppActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        collectData()
    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.appUiState.map {
                    it.apps
                }.collect {
                    (binding.rvApp.adapter as AppAdapter).submitList(it)
                }
            }
        }
    }

    private fun initView() {
        binding.rvApp.adapter = AppAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this@AppActivity, SettingsActivity::class.java)
                intent.action = Intent.ACTION_VIEW
                startActivity(intent)
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}