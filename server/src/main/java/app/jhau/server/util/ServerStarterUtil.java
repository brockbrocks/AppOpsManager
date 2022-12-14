package app.jhau.server.util;

import android.content.Context;

import app.jhau.server.BuildConfig;

public class ServerStarterUtil {
    private static final String starterName = BuildConfig.SERVER_ID + ".ServerStarter";

    public static String getCommand(Context context) {
        String packagePath = context.getPackageResourcePath();
        return "adb shell app_process -Djava.class.path=" + packagePath + " /system/bin " + starterName;
    }
}
