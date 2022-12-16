package app.jhau.server;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManagerApi;
import android.os.Build;

import app.jhau.server.util.Constants;

public class ServerStarter {
    private static final String TAG = Constants.DEBUG_TAG;

    public static void main(String[] args) throws Throwable {
        System.out.println("ServerStarter execute.");
        try {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                startServerNative();
            } else {
                startAppOpsServer();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    static void startAppOpsServer() throws Throwable {
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
                "--nice-name=" + Constants.SERVER_NICK_NAME,
                Constants.SERVER_CLASSNAME
        };
        String oldLibPath = System.getProperty("java.library.path");
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.environment().put("LD_LIBRARY_PATH", oldLibPath + ":" + appInfo.sourceDir + "!/lib/" + Constants.CPU_ABI);
        pb.start();
    }

    static {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            String classPath = System.getProperty("java.class.path");
            String libPath = classPath + "!/lib/" + Build.SUPPORTED_ABIS[0] + "/libserver.so";
            System.load(libPath);
        }

    }

    private native static void startServerNative();

}
