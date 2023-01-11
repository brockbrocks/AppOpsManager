package app.jhau.server;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;

import app.jhau.server.provider.ServerProvider;

public class AppOpsServerManager {
    private static final String TAG = "AppOpsServerManager";

    public static List<ApplicationInfo> getInstalledApplications(Application application) {
        try {
            IAppOpsServer appOpsServer = getAppOpsServerBinder(application);
            if (appOpsServer == null) return null;
            return appOpsServer.getInstalledApplications();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void killServer(Application application) {
        try {
            IAppOpsServer appOpsServer = getAppOpsServerBinder(application);
            if (appOpsServer == null) return;
            appOpsServer.killServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static IAppOpsServer getAppOpsServerBinder(Application application) {
        try {
            Method method = application.getClass().getMethod("getIAppOpsServer");
            return (IAppOpsServer) method.invoke(application);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String execCommand(Application application, String cmd) {
        try {
            IAppOpsServer appOpsServer = getAppOpsServerBinder(application);
            if (appOpsServer == null) return null;
            return appOpsServer.execCommand(cmd);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

}
