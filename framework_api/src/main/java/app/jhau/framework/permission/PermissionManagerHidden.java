package app.jhau.framework.permission;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.permission.IPermissionManager;

import androidx.annotation.NonNull;

public class PermissionManagerHidden extends IPermissionManagerHidden.Stub implements Parcelable {
    private IPermissionManagerHidden mRemote;
    private IPermissionManager permMgr;

    public PermissionManagerHidden() {
        permMgr = IPermissionManager.Stub.asInterface(ServiceManager.getService("permissionmgr"));
    }

    public PermissionManagerHidden(Parcel in) {
        mRemote = IPermissionManagerHidden.Stub.asInterface(in.readStrongBinder());
    }

    public static final Creator<PermissionManagerHidden> CREATOR = new Creator<PermissionManagerHidden>() {
        @Override
        public PermissionManagerHidden createFromParcel(Parcel in) {
            return new PermissionManagerHidden(in);
        }

        @Override
        public PermissionManagerHidden[] newArray(int size) {
            return new PermissionManagerHidden[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeStrongBinder(this.asBinder());
    }

    @Override
    public int getPermissionFlags(String permName, String packageName, int userId) throws RemoteException {
        if (mRemote != null) {
            return mRemote.getPermissionFlags(permName, packageName, userId);
        }
        return permMgr.getPermissionFlags(permName, packageName, userId);
    }

    @Override
    public int getPermissionFlagsApi33(String packageName, String permissionName, int userId) throws RemoteException {
        if (mRemote != null) {
            return mRemote.getPermissionFlagsApi33(packageName, permissionName, userId);
        }
        return permMgr.getPermissionFlags(packageName, permissionName, userId);
    }

    @Override
    public void grantRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
        if (mRemote != null) {
            mRemote.grantRuntimePermission(packageName, permissionName, userId);
            return;
        }
        permMgr.grantRuntimePermission(packageName, permissionName, userId);
    }

    @Override
    public void revokeRuntimePermission(String packageName, String permissionName, int userId, String reason) throws RemoteException {
        if (mRemote != null) {
            mRemote.revokeRuntimePermission(packageName, permissionName, userId, reason);
            return;
        }
        permMgr.revokeRuntimePermission(packageName, permissionName, userId, reason);
    }
}
