package app.jhau.appopsmanager.ui.guide

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.jhau.appopsmanager.IServerObserver
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.app.AppActivity
import app.jhau.appopsmanager.ui.appdetail.AppDetailActivity
import app.jhau.server.provider.ServerProvider
import app.jhau.server.util.StarterUtil

class GuideActivity : AppCompatActivity() {

    private val TAG = "GuideActivity"
    private val iServerObserver = object : IServerObserver.Stub() {
        override fun onServerActivated() {
            val intent = Intent(this@GuideActivity, AppActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.action = Intent.ACTION_VIEW
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        initView()
        //register observer
        contentResolver.call(
            Uri.parse(ServerProvider.AUTHORITY_URI),
            ServerProvider.Method.ADD_SERVER_OBSERVER.str,
            null,
            Bundle().apply {
                putBinder(
                    ServerProvider.Method.ADD_SERVER_OBSERVER.key,
                    iServerObserver.asBinder()
                )
            }
        )
    }

    private fun initView() {
        supportActionBar?.apply {
            title = "Guide"
        }
        findViewById<TextView>(R.id.tv_line2)?.text = StarterUtil.getCommand(this)
        findViewById<Button>(R.id.btn_copy)?.setOnClickListener {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val data = ClipData.newPlainText("cmd", StarterUtil.getCommand(this@GuideActivity))
            clipboardManager.setPrimaryClip(data)
            Toast.makeText(this@GuideActivity, "Copy success", Toast.LENGTH_SHORT).show()
        }
    }
}