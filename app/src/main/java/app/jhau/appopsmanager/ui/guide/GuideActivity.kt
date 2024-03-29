package app.jhau.appopsmanager.ui.guide

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jhau.appopsmanager.App
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.app.AppActivity
import app.jhau.server.IServerActivatedObserver
import app.jhau.server.util.StarterUtil
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class GuideActivity : AppCompatActivity() {
    private var front = AtomicBoolean(false)
    private var activeEventCount = AtomicInteger(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        initView()
        //Register ServerActivatedObserver
        (application as App).iServerActivatedObserver = object : IServerActivatedObserver.Stub() {
            override fun onActivated() {
                activeEventCount.incrementAndGet()
                if (front.get()) startAppActivity()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        front.set(true)
        if (activeEventCount.get() > 0) startAppActivity()
    }

    override fun onPause() {
        super.onPause()
        front.set(false)
    }

    private fun startAppActivity() {
        val intent = Intent(applicationContext, AppActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        startActivity(intent)
        finish()
    }

    private fun initView() {
        supportActionBar?.hide()
        findViewById<TextView>(R.id.tv_line2)?.text = StarterUtil.getCommand(this)
        findViewById<Button>(R.id.btn_copy)?.setOnClickListener {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val data = ClipData.newPlainText("cmd", StarterUtil.getCommand(this@GuideActivity))
            clipboardManager.setPrimaryClip(data)
            Toast.makeText(this@GuideActivity, "Copy success", Toast.LENGTH_SHORT).show()
        }
    }
}