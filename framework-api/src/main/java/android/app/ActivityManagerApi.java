package android.app;

import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.os.ServiceManager;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

public class ActivityManagerApi {

    @RequiresApi(Build.VERSION_CODES.N)
    public static final int UID_OBSERVER_PROCSTATE = 1 << 0;
    @RequiresApi(Build.VERSION_CODES.N)
    public static final int UID_OBSERVER_GONE = 1 << 1;
    @RequiresApi(Build.VERSION_CODES.N)
    public static final int UID_OBSERVER_IDLE = 1 << 2;
    @RequiresApi(Build.VERSION_CODES.N)
    public static final int UID_OBSERVER_ACTIVE = 1 << 3;


    private static final IBinder am = ServiceManager.getService(Context.ACTIVITY_SERVICE);

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
            if (method.getName().equals("getContentProviderExternal")) {
                method.setAccessible(true);
                return method.invoke(am, name, userId, token, tag);
            }
        }
        throw new IllegalStateException();
    }

    public static void registerProcessObserver(IProcessObserver observer) throws Throwable {
        IActivityManager am;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            am = ActivityManagerNative.getDefault();
        } else {
            am = IActivityManager.Stub.asInterface(ActivityManagerApi.am);
        }
        am.registerProcessObserver(observer);
    }

    public static void registerUidObserver(IUidObserver observer) throws Throwable {
        IActivityManager am = getService();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            am.registerUidObserver(observer);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            am.registerUidObserver(observer, UID_OBSERVER_PROCSTATE | UID_OBSERVER_GONE | UID_OBSERVER_IDLE | UID_OBSERVER_ACTIVE);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            am.registerUidObserver(observer, 0, 0, "");
        }
    }

    private static IActivityManager getService() {
        IActivityManager am;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            am = ActivityManagerNative.getDefault();
        } else {
            am = IActivityManager.Stub.asInterface(ActivityManagerApi.am);
        }
        return am;
    }

}
