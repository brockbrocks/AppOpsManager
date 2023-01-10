package android.app;

import android.content.Context;
import android.os.Build;
import android.os.IBinder;
import android.os.ServiceManager;
import android.util.Log;

import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

import app.jhau.framework.ContentProviderHolderWrapper;
import app.jhau.framework_api.BuildConfig;

public class ActivityManagerApi {

    private static final IBinder am = ServiceManager.getService(Context.ACTIVITY_SERVICE);

    @DeprecatedSinceApi(api = Build.VERSION_CODES.Q)
    public static ContentProviderHolderWrapper getContentProviderExternalNew(String name, int userId, IBinder token) throws Throwable {
        IActivityManager am = getService();
        logMethod(am, "getContentProviderExternal");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            Method method = am.getClass().getDeclaredMethod("getContentProviderExternal", String.class, int.class, IBinder.class);
            method.setAccessible(true);
            return new ContentProviderHolderWrapper(method.invoke(am, name, userId, token));
        }
        return new ContentProviderHolderWrapper(am.getContentProviderExternal(name, userId, token));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static android.app.ContentProviderHolder getContentProviderExternalNew(String name, int userId, IBinder token, String tag) throws Throwable {
        IActivityManager am = getService();
        logMethod(am, "getContentProviderExternal");
        return am.getContentProviderExternal(name, userId, token, tag);
    }

    public static Object getContentProviderExternal(String name, int userId, IBinder token) throws Throwable {
        Class<?> cls = Class.forName("android.app.ActivityManagerNative");
        Method getDefaultMethod = cls.getDeclaredMethod("getDefault");
        getDefaultMethod.setAccessible(true);
        Object am = getDefaultMethod.invoke(null);
        logMethod(am, "getContentProviderExternal");
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

    private static IActivityManager getService() {
        IActivityManager am;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
            am = ActivityManagerNative.getDefault();
        } else {
            am = IActivityManager.Stub.asInterface(ActivityManagerApi.am);
        }
        return am;
    }

    private static void logMethod(Object obj, String methodName) {
        if (BuildConfig.DEBUG) {
            for (Method method : obj.getClass().getDeclaredMethods()) {
                if (method.getName().equals(methodName))
                    Log.i("logMethod", methodName + "=" + method);
            }
        }
    }


}
