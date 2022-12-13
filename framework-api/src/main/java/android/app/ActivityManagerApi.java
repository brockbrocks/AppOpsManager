package android.app;

import android.content.Context;
import android.content.pm.IPackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
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
            if (method.getName().equals("getContentProviderExternal")) {
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


    public static void registerProcessObserver(IProcessObserver observer) throws RemoteException, Throwable {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            Class<?> cls = Class.forName("android.app.ActivityManagerNative");
            Method method = cls.getMethod("getDefault");
            Object am = method.invoke(null, null);
            Method registerMethod = cls.getMethod("registerProcessObserver", IProcessObserver.class);
            registerMethod.invoke(am, observer);
        } else {
            IActivityManager am = IActivityManager.Stub.asInterface(ActivityManagerApi.am);
            am.registerProcessObserver(observer);
        }
    }

}
