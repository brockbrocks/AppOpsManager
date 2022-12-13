package android.content.pm;

import android.os.Build;
import android.os.IBinder;
import android.os.ServiceManager;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

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
        throw new IllegalStateException();
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
}
