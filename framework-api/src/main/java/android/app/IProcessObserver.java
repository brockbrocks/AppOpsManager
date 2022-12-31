package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IProcessObserver extends IInterface {

    public static abstract class Stub extends Binder implements IProcessObserver {

        public static IProcessObserver asInterface(IBinder obj) {
            return null;
        }

        @Override
        public IBinder asBinder() {
            return this;
        }
    }

    //M,N,O,P,Q,R,S,T
    public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) throws RemoteException;

    //M,N
    public void onProcessStateChanged(int pid, int uid, int procState) throws RemoteException;

    //M,N,O,P,Q,R,S,T
    public void onProcessDied(int pid, int uid) throws RemoteException;

    //Q,R,S,T
    public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) throws RemoteException;
}
