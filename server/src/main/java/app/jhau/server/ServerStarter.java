package app.jhau.server;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManagerApi;
import android.os.Build;
import android.util.Log;

import app.jhau.server.util.Constants;

public class ServerStarter {
    private static final String TAG = Constants.DEBUG_TAG;

    public static void main(String[] args) throws Throwable {
        System.out.println("Server starter");
        //System.out.println("Test jni. add(1, 4)=" + add(1, 4));

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            int pid = 2;
//            int pid = fork();
            if (pid < 0) Log.i(TAG, "Server fork failed.");
            if (pid > 0) Log.i(TAG, "Child process pid=" + pid);
            if (pid == 0) startAppOpsServer();
        } else {
            startAppOpsServer();
        }
    }

    private static void startAppOpsServer() throws Throwable {
        ApplicationInfo appInfo;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0, 0);
        } else {
            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0L, 0);
        }
        String[] cmd = new String[]{
                "app_process",
                "-Djava.class.path=" + appInfo.sourceDir,
                "/system/bin",
                "--nice-name=" + AppOpsServer.SERVER_NICK_NAME,
                "app.jhau.server.AppOpsServer"
        };
        String oldLibPath = System.getProperty("java.library.path");
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.environment().put("LD_LIBRARY_PATH", oldLibPath + ":" + appInfo.sourceDir + "!/lib/" + Constants.CPU_ABI);
        pb.start();
    }

    static {
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            System.loadLibrary("server");
//        }
    }
//
//    static native int fork();
//
    static native int add(int a, int b);

}
