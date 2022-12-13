package android.app;

import android.content.Context;
import android.content.pm.IPackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.ServiceManager;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

public class ActivityManagerApi {

    private static final IBinder am = ServiceManager.getService(Context.ACTIVITY_SERVICE);

    public static IBinder getService() {
        return am;
    }

    public static Object getContentProviderExternal(String name, int userId, IBinder token) throws Throwable {
        Class<?> cls = Class.forName("android.app.ActivityManagerNative");
        Method getDefaultMethod = cls.getDeclaredMethod("getDefault");
        getDefaultMethod.setAccessible(true);
        Object am = getDefaultMethod.invoke(null);
        for (Method method : am.getClass().getDeclaredMethods()) {
            if (method.getName().equals("getContentProviderExternal")) {
                method.setAccessible(true);
                return method.invoke(am, name, userId, token);
            }
        }
        throw new IllegalStateException();
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    public static Object getContentProviderExternal(String name, int userId, IBinder token, String tag) throws Throwable {
        Object am = IActivityManager.Stub.asInterface(ActivityManagerApi.am);
        for (Method method : am.getClass().getDeclaredMethods()) {
            if (method.getName().equals("getContentProviderExternal")){
                method.setAccessible(true);
                return method.invoke(am, name, userId, token, tag);
            }
        }
        throw new IllegalStateException();
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
