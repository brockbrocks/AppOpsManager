package android.content.pm;

import android.content.pm.ProviderInfo;

interface IPackageManager {
    ProviderInfo resolveContentProvider(String name, int flags, int userId);
    int getPackageUid(String packageName, int flags, int userId);
}
