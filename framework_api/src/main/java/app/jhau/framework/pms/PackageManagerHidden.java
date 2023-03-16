package app.jhau.framework.pms;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.util.List;

import app.jhau.framework.util.ReflectUtil;

public class PackageManagerHidden extends IPackageManagerHidden.Stub implements Parcelable {
    private IPackageManagerHidden mRemote;
    private IPackageManager pm;

    @SuppressLint("DeprecatedSinceApi")
    @Override
    public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws RemoteException {
        if (mRemote != null) return mRemote.getApplicationInfo(packageName, flags, userId);
        return pm.getApplicationInfo(packageName, flags, userId);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public ApplicationInfo getApplicationInfoApi33(String packageName, long flags, int userId) throws RemoteException {
        if (mRemote != null) return mRemote.getApplicationInfoApi33(packageName, flags, userId);
        return pm.getApplicationInfo(packageName, flags, userId);
    }

    @SuppressLint("DeprecatedSinceApi")
    public List<PackageInfo> getInstalledPackages(int flags, int userId) throws RemoteException {
        if (mRemote != null) return mRemote.getInstalledPackages(flags, userId);
        return pm.getInstalledPackages(flags, userId).getList();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public List<PackageInfo> getInstalledPackagesApi33(long flags, int userId) throws RemoteException {
        if (mRemote != null) return mRemote.getInstalledPackagesApi33(flags, userId);
        return pm.getInstalledPackages(flags, userId).getList();
    }

    @SuppressLint("DeprecatedSinceApi")
    @Override
    public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
        if (mRemote != null) return mRemote.getPackageInfo(packageName, flags, userId);
        return pm.getPackageInfo(packageName, flags, userId);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public PackageInfo getPackageInfoApi33(String packageName, long flags, int userId) throws RemoteException {
        if (mRemote != null) return mRemote.getPackageInfoApi33(packageName, flags, userId);
        return pm.getPackageInfo(packageName, flags, userId);
    }

    @SuppressLint("DeprecatedSinceApi")
    @Override
    public int getPermissionFlags(String permissionName, String packageName, int userId) throws RemoteException {
        if (mRemote != null) return mRemote.getPermissionFlags(permissionName, packageName, userId);
        return pm.getPermissionFlags(permissionName, packageName, userId);
    }

    @Override
    public void grantRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
        if (mRemote != null) {
            mRemote.grantRuntimePermission(packageName, permissionName, userId);
            return;
        }
        pm.grantRuntimePermission(packageName, permissionName, userId);
    }

    @Override
    public void revokeRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
        if (mRemote != null) {
            mRemote.revokeRuntimePermission(packageName, permissionName, userId);
            return;
        }
        pm.revokeRuntimePermission(packageName, permissionName, userId);
    }

    @Override
    public boolean isPackageAvailable(String packageName, int userId) throws RemoteException {
        if (mRemote != null) return mRemote.isPackageAvailable(packageName, userId);
        return pm.isPackageAvailable(packageName, userId);
    }

    @SuppressLint("DeprecatedSinceApi")
    @Override
    public List<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags, int userId) throws RemoteException {
        if (mRemote != null) return mRemote.queryIntentActivities(intent, resolvedType, flags, userId);
        return pm.queryIntentActivities(intent, resolvedType, flags, userId).getList();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public List<ResolveInfo> queryIntentActivitiesApi33(Intent intent, String resolvedType, long flags, int userId) throws RemoteException {
        if (mRemote != null) return mRemote.queryIntentActivitiesApi33(intent, resolvedType, flags, userId);
        return pm.queryIntentActivities(intent, resolvedType, flags, userId).getList();
    }

    @Override
    public void setApplicationEnabledSetting(String packageName, int newState, int flags, int userId, String callingPackage) throws RemoteException {
        if (mRemote != null) {
            mRemote.setApplicationEnabledSetting(packageName, newState, flags, userId, callingPackage);
            return;
        }
        pm.setApplicationEnabledSetting(packageName, newState, flags, userId, callingPackage);
    }

    public PackageManagerHidden() {
        IBinder binder = ServiceManager.getService("package");
        pm = IPackageManager.Stub.asInterface(binder);
    }

    protected PackageManagerHidden(Parcel in) {
        mRemote = IPackageManagerHidden.Stub.asInterface(in.readStrongBinder());
    }

    public static final Creator<PackageManagerHidden> CREATOR = new Creator<PackageManagerHidden>() {
        @Override
        public PackageManagerHidden createFromParcel(Parcel in) {
            return new PackageManagerHidden(in);
        }

        @Override
        public PackageManagerHidden[] newArray(int size) {
            return new PackageManagerHidden[size];
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

    private static final String TAG = "tttt";
    public static int FLAG_PERMISSION_SYSTEM_FIXED;
    public static int FLAG_PERMISSION_POLICY_FIXED;

    static {
        for (Field field : PackageManager.class.getFields()) {
            if (field.getName().equals("FLAG_PERMISSION_SYSTEM_FIXED")) {
                FLAG_PERMISSION_SYSTEM_FIXED = ReflectUtil.getStaticField(field);
                Log.i(TAG, field.getName() + "=" + FLAG_PERMISSION_SYSTEM_FIXED);
            }
            if (field.getName().equals("FLAG_PERMISSION_POLICY_FIXED")) {
                FLAG_PERMISSION_POLICY_FIXED = ReflectUtil.getStaticField(field);
                Log.i(TAG, field.getName() + "=" + FLAG_PERMISSION_POLICY_FIXED);
            }
        }
    }
}
