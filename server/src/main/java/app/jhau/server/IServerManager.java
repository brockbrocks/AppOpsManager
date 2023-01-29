package app.jhau.server;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.RemoteException;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import app.jhau.framework.appops.AppOpsManagerHidden;


public class IServerManager {

    public static AppOpsManagerHidden getIAppOpsManagerHidden(Application application) {
        try {
            IServer iServer = getIServerBinder(application);
            if (iServer != null) {
                return iServer.getIAppOpsManagerHidden();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    public static List<ApplicationInfo> getInstalledApplications(Application application) {
        try {
            IServer iServer = getIServerBinder(application);
            if (iServer != null) return iServer.getInstalledApplicationList();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void killServer(Application application) {
        try {
            IServer iServer = getIServerBinder(application);
            if (iServer == null) return;
            iServer.killServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static IServer getIServerBinder(Application application) {
        try {
            Method method = application.getClass().getMethod("getIServer");
            return (IServer) method.invoke(application);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String execCommand(Application application, String cmd) {
        try {
            IServer appOpsServer = getIServerBinder(application);
            if (appOpsServer == null) return null;
            return appOpsServer.execCommand(cmd);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    public static List<PackageInfo> getInstalledPackageInfoList(@NotNull Application application) {
        try {
            IServer iServer = getIServerBinder(application);
            if (iServer == null) return new ArrayList<>();
            return iServer.getInstalledPackageInfoList();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
