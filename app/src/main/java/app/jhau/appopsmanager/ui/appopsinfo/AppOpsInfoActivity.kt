package app.jhau.appopsmanager.ui.appopsinfo

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.jhau.appopsmanager.R
import app.jhau.appopsmanager.databinding.ActivityAppOpsInfoBinding
import app.jhau.appopsmanager.ui.base.BaseBindingActivity

class AppOpsInfoActivity : BaseBindingActivity<ActivityAppOpsInfoBinding, AppOpsInfoViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.appops_list, AppOpsInfoFragment())
            .commit()
    }

    companion object {
        const val PACKAGEINFO = "packageInfo"

        fun start(context: Context, packageInfo: PackageInfo) {
            val intent = Intent(context, AppOpsInfoActivity::class.java)
            intent.action = Intent.ACTION_VIEW
            intent.putExtra(PACKAGEINFO, packageInfo)
            context.startActivity(intent)
        }
    }
}