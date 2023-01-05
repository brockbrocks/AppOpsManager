package app.jhau.appopsmanager.ui.appdetail

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.jhau.appopsmanager.R

class AppDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
        }
        setContentView(R.layout.activity_app_detail)
    }

    companion object {
        fun start(context: Context, info: ApplicationInfo) {
            val intent = Intent(context, AppDetailActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            context.startActivity(intent)
        }
    }
}