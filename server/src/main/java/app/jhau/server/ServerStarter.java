package app.jhau.server;

import android.app.ActivityManagerApi;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManagerApi;
import android.os.Build;
import android.os.Looper;

import app.jhau.server.util.BinderSender;
import app.jhau.server.util.Constants;
import app.jhau.server.util.LogUtil;

public class ServerStarter {
    private static final String TAG = Constants.DEBUG_TAG;
    private static final AppOpsServer appOpsServer = new AppOpsServer();

    public static void main(String[] args) {
        //Log.i(TAG, "app.jhau.server.ServerStarter.main(), pid=" + android.os.Process.myPid() + ", uid=" + android.os.Process.myUid() + " " + Calendar.getInstance().getTime().getTime());
        //Log.i(TAG, "app.jhau.server.ServerStarter.main(), args.length=" + args.length);
        try {
            for (String arg : args) {
                //Log.i(TAG, "app.jhau.server.ServerStarter.main(), arg=" + arg);
                if (arg.equals("--fork-server=true")) {
                    initServer();
                }
            }
            String sourceDir = getApplicationInfo().sourceDir;
            String[] cmd = new String[]{
                    "app_process",
                    "-Djava.class.path=" + sourceDir,
                    "/system/bin",
                    "--nice-name=appops_server",
                    "app.jhau.server.ServerStarter",
                    "--fork-server=true"
            };
            Runtime.getRuntime().exec(cmd);
        } catch (Throwable e) {
            LogUtil.appendToFile(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initServer() throws Throwable {
        //prepare
        Looper.prepare();
        ApplicationInfo appInfo = getApplicationInfo();
        ActivityManagerApi.registerProcessObserver(new IProcessObserverImpl(appInfo.uid, appOpsServer));
        BinderSender.sendBinder(appOpsServer);
        //start loop
        Looper.loop();
    }

    private static ApplicationInfo getApplicationInfo() throws Throwable {
        ApplicationInfo appInfo;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0, 0);
        } else {
            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0L, 0);
        }
        return appInfo;
    }

}
