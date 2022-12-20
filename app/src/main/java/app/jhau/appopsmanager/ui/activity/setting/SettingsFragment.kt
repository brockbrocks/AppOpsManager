package app.jhau.appopsmanager.ui.activity.setting

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.activity.terminal.TerminalActivity
import app.jhau.server.AppOpsServerManager

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        findPreference<Preference>("kill_server")?.setOnPreferenceClickListener {
            AppOpsServerManager.killServer(context)
            true
        }
        findPreference<Preference>("shell_terminal")?.setOnPreferenceClickListener {
            context?.let {
                val intent = Intent(context, TerminalActivity::class.java)
                intent.action = Intent.ACTION_VIEW
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                startActivity(intent)
            }
            true
        }
    }
}