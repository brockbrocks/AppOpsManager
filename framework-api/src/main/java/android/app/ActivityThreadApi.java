package android.app;

import android.content.pm.IPackageManager;
import android.os.Handler;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActivityThreadApi {

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

}
