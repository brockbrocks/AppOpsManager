package app.jhau.server;

import android.app.IUidObserver;
import android.os.RemoteException;
import android.util.Log;

import app.jhau.server.util.Constants;

public class IUidObserverImpl extends IUidObserver.Stub {
    private final String TAG = Constants.DEBUG_TAG;

    @Override
    public void onUidGone(int uid) throws RemoteException {
        Log.i(TAG, "onUidGone: uid=" + uid);
    }

    @Override
    public void onUidGone(int uid, boolean disabled) throws RemoteException {
        Log.i(TAG, "onUidGone: uid=" + uid + ", disabled" + disabled);
    }

    @Override
    public void onUidStateChanged(int uid, int procState) throws RemoteException {
        Log.i(TAG, "onUidStateChanged: " + uid);
    }

    @Override
    public void onUidStateChanged(int uid, int procState, long procStateSeq) throws RemoteException {
        Log.i(TAG, "onUidStateChanged: " + uid);
    }

    @Override
    public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) throws RemoteException {
        Log.i(TAG, "onUidStateChanged: " + uid);
    }

    @Override
    public void onUidActive(int uid) throws RemoteException {
        Log.i(TAG, "onUidActive: uid=" + uid);
    }

    @Override
    public void onUidIdle(int uid) throws RemoteException {
        Log.i(TAG, "onUidIdle: uid=" + uid);
    }

    @Override
    public void onUidIdle(int uid, boolean disabled) throws RemoteException {
        Log.i(TAG, "onUidIdle: " + uid + ", disabled=" + disabled);
    }

    @Override
    public void onUidProcAdjChanged(int uid) throws RemoteException {

    }

    @Override
    public void onUidCachedChanged(int uid, boolean cached) throws RemoteException {

    }
}
