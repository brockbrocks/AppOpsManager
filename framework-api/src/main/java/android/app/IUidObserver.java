package android.app;

import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;

public interface IUidObserver extends IInterface {

    public static abstract class Stub extends Binder implements IUidObserver {

        public static IUidObserver asInterface(IBinder obj) {
            return null;
        }

        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    //Android M,N
    void onUidGone(int uid) throws RemoteException;

    //Android O,P,Q,R,S,T
    @RequiresApi(Build.VERSION_CODES.O)
    void onUidGone(int uid, boolean disabled) throws RemoteException;


    //Android M,N
    void onUidStateChanged(int uid, int procState) throws RemoteException;

    //Android O,P,Q
    void onUidStateChanged(int uid, int procState, long procStateSeq) throws RemoteException;

    //Android R,S,T
    void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) throws RemoteException;


    //Android N,O,P,Q,R,S,T
    void onUidActive(int uid) throws RemoteException;

    //Android N
    void onUidIdle(int uid) throws RemoteException;
    //Android O,P,Q,R,S,T
    void onUidIdle(int uid, boolean disabled) throws RemoteException;

    //Android T
    void onUidProcAdjChanged(int uid) throws RemoteException;

    //Android O,P,Q,R,S,T
    @RequiresApi(Build.VERSION_CODES.O)
    void onUidCachedChanged(int uid, boolean cached) throws RemoteException;
}