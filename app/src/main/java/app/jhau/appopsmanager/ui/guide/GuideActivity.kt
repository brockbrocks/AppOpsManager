package app.jhau.appopsmanager.ui.guide

import android.content.ClipData
import android.content.ClipboardManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import app.jhau.appopsmanager.R
import app.jhau.server.IAppOpsServer
import app.jhau.server.provider.ServerProvider
import app.jhau.server.util.StarterUtil
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuideActivity : AppCompatActivity() {

    private val TAG = "GuideActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)
        initView()
    }

    private fun initView() {

        findViewById<Button>(R.id.btn_copy)?.setOnClickListener {
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val data = ClipData.newPlainText("cmd", StarterUtil.getCommand(this@GuideActivity))
            clipboardManager.setPrimaryClip(data)
            Toast.makeText(this@GuideActivity, "Copy success", Toast.LENGTH_SHORT).show()
        }
    }
}