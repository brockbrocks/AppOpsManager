package app.jhau.appopsmanager.ui.apppermsinfo

import android.content.pm.PackageInfo
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

class AppPermsInfoViewModel @AssistedInject constructor(
    @Assisted pkgInfo: PackageInfo
): ViewModel() {


    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(pkgInfo: PackageInfo): AppPermsInfoViewModel
    }
}