package app.jhau.server.util;

import android.content.Context;

import app.jhau.server.BuildConfig;

public class ServerStarterUtil {
    private static final String starterName = BuildConfig.SERVER_ID + ".ServerStarter";
    private static final String serverNickName = "appops_server";

    public static String getCommand(Context context) {
        String packagePath = context.getPackageResourcePath();
        return "adb shell app_process -Djava.class.path=" + packagePath + " /system/bin --nice-name=" + serverNickName + " " + starterName;
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
//            return "adb shell sh /sdcard/Android/data/" + Constants.APPLICATION_ID + "/cache/starter.sh";
//        } else {
//            String packagePath = context.getPackageResourcePath();
//            return "adb shell app_process -Djava.class.path=" + packagePath + " /system/bin --nice-name=" + serverNickName + " " + starterName;
//        }
    }
}
