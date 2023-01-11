package android.app;

import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.RequiresApi;

public interface IProcessObserver extends IInterface {

    abstract class Stub extends Binder implements IProcessObserver {

        public static IProcessObserver asInterface(IBinder obj) {
            return null;
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    }

    void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) throws RemoteException;

    @DeprecatedSinceApi(api = Build.VERSION_CODES.O)
    void onProcessStateChanged(int pid, int uid, int procState) throws RemoteException;

    void onProcessDied(int pid, int uid) throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.Q)
    void onForegroundServicesChanged(int pid, int uid, int serviceTypes) throws RemoteException;
}
