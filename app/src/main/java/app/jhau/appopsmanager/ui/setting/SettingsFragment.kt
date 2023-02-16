package app.jhau.appopsmanager.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.guide.GuideActivity
import app.jhau.appopsmanager.ui.terminal.TerminalActivity
import app.jhau.server.IServerManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var iServerManager: IServerManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        findPreference<Preference>("kill_server")?.setOnPreferenceClickListener {
            iServerManager.killServer()
            val intent = Intent(this@SettingsFragment.context, GuideActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            startActivity(intent)
            requireActivity().finish()
            true
        }
        findPreference<Preference>("shell_terminal")?.setOnPreferenceClickListener {
            context?.let {
                val intent = Intent(context, TerminalActivity::class.java)
                intent.action = Intent.ACTION_VIEW
                startActivity(intent)
            }
            true
        }
    }
}