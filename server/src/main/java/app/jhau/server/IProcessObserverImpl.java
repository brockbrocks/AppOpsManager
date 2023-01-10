package app.jhau.server;

import android.app.IProcessObserver;
import android.content.pm.PackageManagerApi;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import app.jhau.server.util.BinderSender;
import app.jhau.server.util.Constants;

public class IProcessObserverImpl extends IProcessObserver.Stub {

    private static final String TAG = Constants.DEBUG_TAG;
    private final int appUid;
    private final IBinder server;

    IProcessObserverImpl(int appUid, IBinder server) {
        this.appUid = appUid;
        this.server = server;
    }

    @Override
    public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) throws RemoteException {
        try {
            String nameForUid = PackageManagerApi.getNameForUid(uid);
            Log.i(TAG, "onForegroundActivitiesChanged: nameForUid=" + nameForUid);
            boolean isAppUidChanged = false;
            if (nameForUid.equals(BuildConfig.APPLICATION_ID)) {
                isAppUidChanged = appUid != uid;
            }
            Log.i(TAG, "onForegroundActivitiesChanged: isAppUidChanged=" + isAppUidChanged);
            //
            if (uid == appUid && foregroundActivities) {
                Log.i(TAG, "onForegroundActivitiesChanged: sendBinder=" + server);
                BinderSender.sendBinder(server);
            }
        } catch (Throwable e) {
            throw new RemoteException(e.getMessage());
        }
    }

    @Override
    public void onProcessStateChanged(int pid, int uid, int procState) throws RemoteException {
    }

    @Override
    public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) throws RemoteException {
    }

    @Override
    public void onProcessDied(int pid, int uid) throws RemoteException {
    }
}
