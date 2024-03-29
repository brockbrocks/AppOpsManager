package app.jhau.server;

import android.annotation.SuppressLint;
import android.os.Build;

import java.io.InputStreamReader;

import app.jhau.server.util.Constants;
@SuppressLint("UnsafeDynamicallyLoadedCode")
public class ServerStarter {
    private static final String TAG = Constants.DEBUG_TAG;

    public static void main(String[] args) throws Throwable {
        System.out.println("ServerStarter execute.");
        try {
            if (checkServerExist()) {
                System.out.println("Server already exists.");
                return;
            }
            startServerNative(System.getProperty("java.class.path"));
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static boolean checkServerExist() throws Throwable {
        String[] cmd = new String[]{"sh", "-c", "ps | grep " + Constants.SERVER_NICK_NAME};
        char[] buf = new char[512];
        InputStreamReader isr = new InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream());
        StringBuilder ret = new StringBuilder();
        int len;
        while ((len = isr.read(buf)) != -1) ret.append(buf, 0, len);
        isr.close();
        return ret.toString().contains(Constants.SERVER_NICK_NAME);
    }

//    static void startAppOpsServer() throws Throwable {
//        ApplicationInfo appInfo;
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
//            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0, 0);
//        } else {
//            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0L, 0);
//        }
//        String[] cmd = new String[]{
//                "app_process",
//                "-Djava.class.path=" + appInfo.sourceDir,
//                "/system/bin",
//                "--nice-name=" + Constants.SERVER_NICK_NAME,
//                Constants.SERVER_CLASSNAME
//        };
//        ProcessBuilder pb = new ProcessBuilder(cmd);
//        pb.start();
//    }

    static {
        String classPath = System.getProperty("java.class.path");
        String libPath = classPath + "!/lib/" + Build.SUPPORTED_ABIS[0] + "/libserver.so";
        System.load(libPath);
    }

    private native static void startServerNative(String apkPath);

}
