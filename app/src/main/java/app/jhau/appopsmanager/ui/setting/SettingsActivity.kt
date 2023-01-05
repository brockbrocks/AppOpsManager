package app.jhau.appopsmanager.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.jhau.appopsmanager.R
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolBar.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                finish()
            }
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.setting_container, SettingsFragment())
            .commit()
    }
}