package app.jhau.framework.ams;

import android.annotation.SuppressLint;
import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.app.IProcessObserver;
import android.content.Context;
import android.content.pm.UserInfo;
import android.os.Build;
import android.os.IBinder;
import android.os.ServiceManager;

import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

public class ActivityManagerApi {

    private static final IBinder am = ServiceManager.getService(Context.ACTIVITY_SERVICE);

    @SuppressLint("DeprecatedSinceApi")
    private static IActivityManager getService() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return ActivityManagerNative.asInterface(am);
        } else {
            return IActivityManager.Stub.asInterface(am);
        }
    }

    @SuppressLint("DeprecatedSinceApi")
    @DeprecatedSinceApi(api = Build.VERSION_CODES.Q)
    public static ContentProviderHolderWrapper getContentProviderExternal(String name, int userId, IBinder token) throws Throwable {
        IActivityManager am = getService();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Method method = am.getClass().getDeclaredMethod("getContentProviderExternal", String.class, int.class, IBinder.class);
            method.setAccessible(true);
            return new ContentProviderHolderWrapper(method.invoke(am, name, userId, token));
        }
        return new ContentProviderHolderWrapper(am.getContentProviderExternal(name, userId, token));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static android.app.ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token, String tag) throws Throwable {
        IActivityManager am = getService();
        return am.getContentProviderExternal(name, userId, token, tag);
    }

    public static void registerProcessObserver(IProcessObserver observer) throws Throwable {
        IActivityManager am = getService();
        am.registerProcessObserver(observer);
    }

    public static UserInfo getCurrentUser(){
        IActivityManager am = getService();
        return am.getCurrentUser();
    }
}
