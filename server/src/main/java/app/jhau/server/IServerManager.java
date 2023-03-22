package app.jhau.server;

import android.app.Application;
import android.os.RemoteException;

import java.lang.reflect.Method;

import app.jhau.framework.appops.AppOpsManagerHidden;
import app.jhau.framework.permission.PermissionManagerHidden;
import app.jhau.framework.pms.PackageManagerHidden;

public class IServerManager {
    private IServer iServer;

    public IServerManager(Application application) {
        try {
            Method method = application.getClass().getMethod("getIServer");
            iServer = (IServer) method.invoke(application);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public AppOpsManagerHidden getAppOpsManagerHidden() {
        try {
            if (iServer != null) {
                return iServer.getAppOpsManagerHidden();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public PackageManagerHidden getPackageManagerHidden() {
        try {
            if (iServer != null) {
                return iServer.getPackageManagerHidden();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public PermissionManagerHidden getPermissionManagerHidden() {
        try {
            if (iServer != null) {
                return iServer.getPermissionManagerHidden();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public void killServer() {
        try {
            if (iServer == null) return;
            iServer.killServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String execCommand(String cmd) {
        try {
            if (iServer != null) return iServer.execCommand(cmd);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }
}
