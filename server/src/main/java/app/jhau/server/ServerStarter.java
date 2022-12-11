package app.jhau.server;

import android.app.ActivityManagerApi;
import android.app.ActivityThreadApi;
import android.app.IActivityManager;
import android.content.pm.IPackageManager;
import android.os.IBinder;
import android.os.Looper;

import app.jhau.server.util.BinderSender;
import app.jhau.server.util.Constants;

public class ServerStarter {
    private static final String TAG = "AppOpsServer";
    private static final AppOpsServer appOpsServer = new AppOpsServer();

    public static void main(String[] args) {
        System.out.println(TAG + " start");
        try {
            Looper.prepare();
            initServer();
            Looper.loop();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void initServer() throws Throwable {
        IPackageManager pm = ActivityThreadApi.getPackageManager();
        int appUid = pm.getPackageUid(Constants.APPLICATION_ID, 0, 0);
        IActivityManager am = ActivityManagerApi.getService();
        am.registerProcessObserver(new IProcessObserverImpl(appUid, appOpsServer));
        BinderSender.sendBinder((IBinder) appOpsServer);
    }

}
