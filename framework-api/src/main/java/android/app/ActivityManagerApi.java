package android.app;

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

    public final ContentProviderHolder getContentProvider(IApplicationThread caller, String callingPackage, String name, int userId, boolean stable) {
        return null;
    }
}
