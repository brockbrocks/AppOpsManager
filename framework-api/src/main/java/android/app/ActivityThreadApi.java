package android.app;

import android.content.pm.IPackageManager;
import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActivityThreadApi {

    public Object getAppThread(ActivityThread thread) {
        return null;
    }

    public static Handler get_mH(ActivityThread thread) {
        try {
            Field mH = thread.getClass().getDeclaredField("mH");
            mH.setAccessible(true);
            return (Handler) mH.get(thread);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ActivityThread systemMain() {
        try {
            Method systemMainMethod = ActivityThread.class.getDeclaredMethod("systemMain");
            systemMainMethod.setAccessible(true);
            return (ActivityThread) systemMainMethod.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //public ApplicationThread getApplicationThread()
    public static IApplicationThread getApplicationThread(ActivityThread thread)
    {
        try {
            Method getApplicationThreadMethod = ActivityThread.class.getDeclaredMethod("getApplicationThread");
            getApplicationThreadMethod.setAccessible(true);
            return (IApplicationThread) getApplicationThreadMethod.invoke(thread);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
