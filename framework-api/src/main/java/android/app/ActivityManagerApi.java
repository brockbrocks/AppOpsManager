package android.app;

import android.content.pm.IPackageManager;

import java.lang.reflect.Method;

public class ActivityManagerApi {
    public static IActivityManager getService() {
        try {
            Method method = ActivityManager.class.getDeclaredMethod("getService");
            method.setAccessible(true);
            return (IActivityManager) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IPackageManager getPackageManager() {
        try {
            Method getPackageManagerMethod = ActivityThread.class.getDeclaredMethod("getPackageManager");
            getPackageManagerMethod.setAccessible(true);
            return (IPackageManager) getPackageManagerMethod.invoke(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

}
