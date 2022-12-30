package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IActivityManager extends IInterface {

    public static abstract class Stub extends Binder implements IActivityManager {

        public static IActivityManager asInterface(IBinder obj) {
            return null;
        }

        @Override
        public IBinder asBinder() {
            return null;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return false;
        }

    }

    public void registerProcessObserver(IProcessObserver observer) throws RemoteException;
    public void unregisterProcessObserver(IProcessObserver observer) throws RemoteException;

    //Android Q and later
    public ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token, String tag) throws RemoteException;
}
