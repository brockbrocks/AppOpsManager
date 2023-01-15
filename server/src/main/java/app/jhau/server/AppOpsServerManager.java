package app.jhau.server;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.RemoteException;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AppOpsServerManager {
    private static final String TAG = "AppOpsServerManager";

    @NotNull
    public static List<ApplicationInfo> getInstalledApplications(Application application) {
        try {
            IAppOpsServer appOpsServer = getAppOpsServerBinder(application);
            if (appOpsServer == null) return new ArrayList<>();
            return appOpsServer.getInstalledApplicationList();
        } catch (Throwable e) {
            e.printStackTrace();
            return new ArrayList<>();
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

    @NotNull
    public static List<PackageInfo> getInstalledPackageInfoList(@NotNull Application application) {
        try {
            IAppOpsServer appOpsServer = getAppOpsServerBinder(application);
            if (appOpsServer == null) return new ArrayList<>();
           return appOpsServer.getInstalledPackageInfoList();
        } catch (RemoteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
