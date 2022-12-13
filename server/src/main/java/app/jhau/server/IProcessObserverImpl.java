package app.jhau.server;

import android.app.IProcessObserver;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import app.jhau.server.util.BinderSender;

public class IProcessObserverImpl extends IProcessObserver.Stub {

    private static final String TAG = "IProcessObserverImpl";
    private final int appUid;
    private final IBinder server;

    IProcessObserverImpl(int appUid, IBinder server) {
        this.appUid = appUid;
        this.server = server;
    }

    @Override
    public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) throws RemoteException {
        if (uid == appUid && foregroundActivities) {
            Log.i("ServerProvider", "onForegroundActivitiesChanged: " + "pid=" + pid + " uid=" + uid + " foregroundActivities=" + foregroundActivities);
            try {
                BinderSender.sendBinder(server);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onProcessStateChanged(int pid, int uid, int procState) throws RemoteException {

    }

    @Override
    public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) throws RemoteException {
        Log.i(TAG, "onForegroundServicesChanged: ");
    }

    @Override
    public void onProcessDied(int pid, int uid) throws RemoteException {
        Log.i(TAG, "onProcessDied: ");
    }
}
