package android.app;

import android.content.pm.PackageManager;
import android.os.UserHandle;

public abstract class ApplicationPackageManager extends PackageManager {
    public abstract int getPermissionFlags(String permName, String packageName, UserHandle user);
}
