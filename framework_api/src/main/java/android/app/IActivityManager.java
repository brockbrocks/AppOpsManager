package android.app;

import android.content.IContentProvider;
import android.content.pm.UserInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.RequiresApi;

public interface IActivityManager extends IInterface {

    abstract class Stub extends Binder implements IActivityManager {

        public static IActivityManager asInterface(IBinder obj) {
            return null;
        }

        @Override
        public IBinder asBinder() {
            return null;
        }

    }

    void registerProcessObserver(IProcessObserver observer) throws RemoteException;

    void unregisterProcessObserver(IProcessObserver observer) throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.O)
    @DeprecatedSinceApi(api = Build.VERSION_CODES.Q)
    android.app.ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token) throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.Q)
    android.app.ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token, String tag) throws RemoteException;

    class ContentProviderHolder {
        public IContentProvider provider;
    }

    UserInfo getCurrentUser();
}
