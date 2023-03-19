package app.jhau.appopsmanager.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.jhau.appopsmanager.App
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.ui.guide.GuideActivity
import app.jhau.appopsmanager.ui.terminal.TerminalActivity
import app.jhau.server.IServerManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var iSvcMgr: IServerManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        findPreference<Preference>("kill_server")?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireActivity()).setTitle("Are you sure?")
                .setPositiveButton("ok") { _, _ -> killServer() }.show()
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

    private fun killServer() {
        try {
            (requireActivity().application as App).onServerKill()
            iSvcMgr.killServer();
            val intent = Intent(requireActivity(), GuideActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}