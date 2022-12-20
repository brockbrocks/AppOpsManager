package app.jhau.appopsmanager.ui.activity.terminal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import app.jhau.appopsmanager.R
import app.jhau.server.AppOpsServerManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText

class TerminalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_termainal)
        findViewById<MaterialToolbar>(R.id.topAppBar)?.apply {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                finish()
            }
        }
        findViewById<Button>(R.id.btn_exec)?.let {
            it.setOnClickListener {
                val etCmdContent = findViewById<TextInputEditText>(R.id.et_cmd_content)
                val ret = AppOpsServerManager.execCommand(
                    applicationContext,
                    etCmdContent.text?.trim().toString()
                )
                findViewById<TextView>(R.id.tv_cmd_ret).text = ret
            }
        }
    }
}