package android.content.pm;

import android.content.pm.ProviderInfo;

interface IPackageManager {
    ApplicationInfo getApplicationInfo(String packageName, int flags ,int userId);
    //int getPackageUid(String packageName, int flags, int userId);
}
