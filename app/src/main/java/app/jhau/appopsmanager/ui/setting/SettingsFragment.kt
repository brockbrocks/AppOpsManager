package app.jhau.appopsmanager.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.guide.GuideActivity
import app.jhau.appopsmanager.ui.terminal.TerminalActivity
import app.jhau.server.AppOpsServerManager

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        findPreference<Preference>("kill_server")?.setOnPreferenceClickListener {
            AppOpsServerManager.killServer(context)
            val intent = Intent(this@SettingsFragment.context, GuideActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            activity?.finish()
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