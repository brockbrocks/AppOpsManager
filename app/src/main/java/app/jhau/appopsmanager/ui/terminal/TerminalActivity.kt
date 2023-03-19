package app.jhau.appopsmanager.ui.terminal

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.jhau.appopsmanager.R
import app.jhau.server.IServerManager
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TerminalActivity : AppCompatActivity() {

    @Inject
    lateinit var iSvcMgr: IServerManager
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
                val cmd = findViewById<TextInputEditText>(R.id.et_cmd_content)
                val ret = iSvcMgr.execCommand(cmd.text.toString().trim())
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