package app.jhau.appopsmanager.ui.activity.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.jhau.appopsmanager.ui.activity.appdetail.AppAdapter
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.activity.setting.SettingsActivity
import app.jhau.appopsmanager.databinding.ActivityHomeBinding
import app.jhau.server.AppOpsServerManager
import com.google.android.material.appbar.MaterialToolbar

class HomeActivity : AppCompatActivity() {
    private val TAG = "HomeActivity"
    private val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.inflate(
            layoutInflater,
            R.layout.activity_home,
            null,
            false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.apply {
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.settings -> {
                        val intent = Intent(this@HomeActivity, SettingsActivity::class.java)
                        intent.action = Intent.ACTION_VIEW
                        startActivity(intent)
                    }
                    else -> {}
                }
                true
            }
        }
        initView()
    }

    private fun initView() {
        val infoList = AppOpsServerManager.getInstalledApplications(applicationContext)
        binding.rvApp.adapter = AppAdapter(infoList)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
}