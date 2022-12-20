package app.jhau.appopsmanager.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import app.jhau.appopsmanager.ui.activity.guide.GuideActivity
import app.jhau.appopsmanager.ui.activity.home.HomeActivity
import app.jhau.server.util.StarterUtil

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { true }
        if (StarterUtil.checkServerExist(applicationContext)) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            startActivity(intent)
        } else {
            val intent = Intent(this, GuideActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            startActivity(intent)
        }
        finish()
    }
}