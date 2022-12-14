package app.jhau.server.util;

import android.content.Context;
import android.os.Build;

import app.jhau.server.BuildConfig;

public class ServerStarterUtil {
    private static final String starterName = BuildConfig.SERVER_ID + ".ServerStarter";

    public static String getCommand(Context context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            return "adb shell sh /sdcard/Android/data/" + Constants.APPLICATION_ID + "/cache/starter.sh";
        } else {
            String packagePath = context.getPackageResourcePath();
            return "adb shell app_process -Djava.class.path=" + packagePath + " /system/bin " + starterName;
        }
    }
}
