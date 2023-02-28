package app.jhau.server;

import android.app.Application;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Method;

import app.jhau.framework.appops.AppOpsManagerHidden;
import app.jhau.framework.pms.PackageManagerHidden;

public class IServerManager {
    private Application application;
    private IServer iServer;
    private final IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            Log.i("tttt", "binderDied: 尝试重新绑定IServer");
            if (iServer == null) return;
            iServer.asBinder().unlinkToDeath(mDeathRecipient, 0);
            try {
                Method method = application.getClass().getMethod("getIServer");
                iServer = (IServer) method.invoke(application);
                iServer.asBinder().linkToDeath(mDeathRecipient, 0);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };

    public IServerManager(Application application) {
        this.application = application;
        try {
            Method method = application.getClass().getMethod("getIServer");
            iServer = (IServer) method.invoke(application);
            iServer.asBinder().linkToDeath(mDeathRecipient, 0);
        } catch (Throwable e) {
            e.printStackTrace();
        }
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

    public void killServer() {
        try {
            if (iServer == null) return;
            iServer.killServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String execCommand(String cmd){
        try {
            if (iServer != null) return iServer.execCommand(cmd);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return "";
    }
}
