package app.jhau.server;

import android.app.ActivityManagerApi;
import android.app.IActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManagerApi;
import android.os.Build;
import android.os.Looper;

import app.jhau.server.util.BinderSender;
import app.jhau.server.util.Constants;
import app.jhau.server.util.LogUtil;

public class ServerStarter {
    private static final String TAG = "AppOpsServer";
    private static final AppOpsServer appOpsServer = new AppOpsServer();

    public static void main(String[] args) {
        try {
            Looper.prepare();
            initServer();
            Looper.loop();
        } catch (Throwable e) {
            LogUtil.appendToFile(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void initServer() throws Throwable {
        ApplicationInfo appInfo;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0, 0);
        } else
            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0L, 0);
        IActivityManager am = IActivityManager.Stub.asInterface(ActivityManagerApi.getService());
        am.registerProcessObserver(new IProcessObserverImpl(appInfo.uid, appOpsServer));
        BinderSender.sendBinder(appOpsServer);
    }

}
