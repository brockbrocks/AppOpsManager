package app.jhau.appopsmanager.ui.terminal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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
        supportActionBar?.apply {
            title = "Shell"
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setDisplayHomeAsUpEnabled(true)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}