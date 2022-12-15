package app.jhau.server;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManagerApi;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

import app.jhau.server.util.Constants;

public class ServerStarter {
    private static final String TAG = Constants.DEBUG_TAG;

    public static void main(String[] args) throws Throwable {
        Log.i(TAG, "Server starter");
        System.out.println("Server starter");
        ApplicationInfo appInfo;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0, 0);
        } else {
            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0L, 0);
        }

//        ProcessBuilder process = new ProcessBuilder(
//                "app_process",
//                "-Djava.class.path=" + appInfo.sourceDir,
//                "/system/bin",
//                /*"--nice-name=" + AppOpsServer.SERVER_NICK_NAME,*/
//                "app.jhau.server.AppOpsServer"
//        );
//        process.start();

        String[] cmd = new String[]{
                "app_process",
                "-Djava.class.path=" + appInfo.sourceDir,
                "/system/bin",
                "--nice-name=" + AppOpsServer.SERVER_NICK_NAME,
                "app.jhau.server.AppOpsServer"
        };
        Runtime.getRuntime().exec(cmd);

        //Runtime.getRuntime().exec("app_process -Djava.class.path=" + appInfo.sourceDir + " /system/bin app.jhau.server.AppOpsServer");
    }
}
