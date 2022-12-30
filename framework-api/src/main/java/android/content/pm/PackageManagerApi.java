package android.content.pm;

import android.os.Build;
import android.os.IBinder;
import android.os.ServiceManager;

import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.util.List;

public class PackageManagerApi {
    private static final IBinder pm = ServiceManager.getService("package");

    public static ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws Throwable {
        IPackageManager pm = IPackageManager.Stub.asInterface(PackageManagerApi.pm);
        for (Method method : pm.getClass().getDeclaredMethods()) {
            if (method.getName().equals("getApplicationInfo")) {
                method.setAccessible(true);
                return (ApplicationInfo) method.invoke(pm, packageName, flags, userId);
            }
        }
        throw new IllegalStateException("ApplicationInfo.getApplicationInfo() unknown exception");
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public static ApplicationInfo getApplicationInfo(String packageName, long flags, int userId) throws Throwable {
        IPackageManager pm = IPackageManager.Stub.asInterface(PackageManagerApi.pm);
        for (Method method : pm.getClass().getDeclaredMethods()) {
            if (method.getName().equals("getApplicationInfo")) {
                method.setAccessible(true);
                return (ApplicationInfo) method.invoke(pm, packageName, flags, userId);
            }
        }
        throw new IllegalStateException();
    }

    public static List<PackageInfo> getInstalledPackages(int flags, int userId) throws Throwable {
        IPackageManager pm = IPackageManager.Stub.asInterface(PackageManagerApi.pm);
        return (List<PackageInfo>) pm.getInstalledPackages(flags, userId).getList();
    }

    @DeprecatedSinceApi(api = Build.VERSION_CODES.TIRAMISU)
    public static List<ApplicationInfo> getInstalledApplications(int flags, int userId) throws Throwable {
        IPackageManager pm = IPackageManager.Stub.asInterface(PackageManagerApi.pm);
        return (List<ApplicationInfo>) pm.getInstalledApplications(flags, userId).getList();
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public static List<ApplicationInfo> getInstalledApplications(long flags, int userId) throws Throwable {
        IPackageManager pm = IPackageManager.Stub.asInterface(PackageManagerApi.pm);
        return (List<ApplicationInfo>) pm.getInstalledApplications(flags, userId).getList();
    }


    public static boolean isPackageAvailable(String packageName, int userId) throws Throwable {
        IPackageManager pm = IPackageManager.Stub.asInterface(PackageManagerApi.pm);
        return pm.isPackageAvailable(packageName, userId);
    }

    public static String getNameForUid(int uid) throws Throwable {
        IPackageManager pm = IPackageManager.Stub.asInterface(PackageManagerApi.pm);
        return pm.getNameForUid(uid);
    }

}
