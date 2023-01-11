package android.content.pm;

import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.RequiresApi;

public interface IPackageManager extends IInterface {
    abstract class Stub extends Binder implements IPackageManager {

        public static IPackageManager asInterface(IBinder obj) {
            return null;
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    }

    @DeprecatedSinceApi(api = Build.VERSION_CODES.TIRAMISU)
    ParceledListSlice getInstalledApplications(int flags, int userId) throws RemoteException;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    ParceledListSlice getInstalledApplications(long flags, int userId) throws RemoteException;

    boolean isPackageAvailable(String packageName, int userId) throws RemoteException;

    String getNameForUid(int uid) throws RemoteException;

    @DeprecatedSinceApi(api = Build.VERSION_CODES.TIRAMISU)
    ApplicationInfo getApplicationInfo(String packageName, int flags ,int userId) throws RemoteException;
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    ApplicationInfo getApplicationInfo(String packageName, long flags ,int userId) throws RemoteException;
}
