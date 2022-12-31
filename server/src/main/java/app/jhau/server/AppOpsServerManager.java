package app.jhau.server;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.List;

import app.jhau.server.provider.ServerProvider;

public class AppOpsServerManager {

    public static List<ApplicationInfo> getInstalledApplications(Context context) {
        IBinder binder = getAppOpsServerBinder(context);
        IAppOpsServer appOpsServer = IAppOpsServer.Stub.asInterface(binder);
        if (appOpsServer == null) return null;
        try {
            return appOpsServer.getInstalledApplications();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void killServer(Context context) {
        try {
            IAppOpsServer appOpsServer = IAppOpsServer.Stub.asInterface(getAppOpsServerBinder(context));
            appOpsServer.killServer();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static IBinder getAppOpsServerBinder(Context context) {
        Bundle bundle = context.getContentResolver().call(Uri.parse(ServerProvider.AUTHORITY_URI), ServerProvider.GET_BINDER, "", null);
        return bundle.getBinder(ServerProvider.SERVER_BINDER_KEY);
    }

    public static String execCommand(Context context, String cmd) {
        try {
            IAppOpsServer appOpsServer = IAppOpsServer.Stub.asInterface(getAppOpsServerBinder(context));
            return appOpsServer.execCommand(cmd);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

}
