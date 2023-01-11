package app.jhau.framework_api;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.ServiceManager;

import androidx.annotation.DeprecatedSinceApi;

import java.util.List;
import java.util.Objects;

public class PackageManagerApi {
    private static final IBinder binder = ServiceManager.getService("package");

    @SuppressLint("DeprecatedSinceApi")
    @DeprecatedSinceApi(api = Build.VERSION_CODES.TIRAMISU)
    public static ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws Throwable {
        IPackageManager pm = IPackageManager.Stub.asInterface(binder);
        return Objects.requireNonNull(pm).getApplicationInfo(packageName, flags, userId);
    }

    @SuppressLint("DeprecatedSinceApi")
    public static ApplicationInfo getApplicationInfo(String packageName, long flags, int userId) throws Throwable {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return getApplicationInfo(packageName, (int) flags, userId);
        }
        IPackageManager pm = IPackageManager.Stub.asInterface(binder);
        return Objects.requireNonNull(pm).getApplicationInfo(packageName, flags, userId);
    }

    @SuppressLint("DeprecatedSinceApi")
    @DeprecatedSinceApi(api = Build.VERSION_CODES.TIRAMISU)
    private static List<ApplicationInfo> getInstalledApplications(int flags, int userId) throws Throwable {
        IPackageManager pm = IPackageManager.Stub.asInterface(binder);
        return Objects.requireNonNull(pm).getInstalledApplications(flags, userId).getList();
    }

    @SuppressLint("DeprecatedSinceApi")
    public static List<ApplicationInfo> getInstalledApplications(long flags, int userId) throws Throwable {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return getInstalledApplications((int) flags, userId);
        }
        IPackageManager pm = IPackageManager.Stub.asInterface(binder);
        return Objects.requireNonNull(pm).getInstalledApplications(flags, userId).getList();
    }

    public static boolean isPackageAvailable(String packageName, int userId) throws Throwable {
        IPackageManager pm = IPackageManager.Stub.asInterface(binder);
        return Objects.requireNonNull(pm).isPackageAvailable(packageName, userId);
    }

    public static String getNameForUid(int uid) throws Throwable {
        IPackageManager pm = IPackageManager.Stub.asInterface(binder);
        return Objects.requireNonNull(pm).getNameForUid(uid);
    }

}
