package android.permission;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IPermissionManager extends IInterface {
    abstract class Stub extends Binder implements IPermissionManager {

        public static IPermissionManager asInterface(IBinder obj) {
            return null;
        }

        @Override
        public IBinder asBinder() {
            return null;
        }
    }

    @Override
    default IBinder asBinder() {
        return null;
    }

    //api31
    //int getPermissionFlags(String permName, String packageName, int userId) throws RemoteException;

    /**
     * !!! prams types different from api31,
     * !!! int getPermissionFlags(String permName, String packageName, int userId) !!!
     */
    int getPermissionFlags(String packageName, String permissionName, int userId) throws RemoteException;

    void grantRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException;

    void revokeRuntimePermission(String packageName, String permissionName, int userId, String reason) throws RemoteException;
}
