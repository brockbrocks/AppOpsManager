package app.jhau.appopsmanager.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import app.jhau.appopsmanager.ui.app.AppActivity
import app.jhau.appopsmanager.ui.guide.GuideActivity
import app.jhau.server.util.StarterUtil

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
//        splashScreen.setKeepOnScreenCondition { true }
        if (StarterUtil.checkServerExist(application)) {
            val intent = Intent(this, AppActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            startActivity(intent)
        } else {
            val intent = Intent(this, GuideActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            startActivity(intent)
        }
        finish()
    }
}