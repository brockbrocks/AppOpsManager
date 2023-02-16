package app.jhau.framework.pms;

import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ParceledListSlice;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;

import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.RequiresApi;

import java.util.List;

public class PackageManagerApi implements IPackageManager {

    private static final PackageManagerApi INSTANCE = new PackageManagerApi();

    private final IPackageManager pm = getService();

    public static PackageManagerApi getInstance() {
        return INSTANCE;
    }

    private IPackageManager getService() {
        IBinder binder = ServiceManager.getService("package");
        return IPackageManager.Stub.asInterface(binder);
    }

    @SuppressLint("DeprecatedSinceApi")
    @DeprecatedSinceApi(api = Build.VERSION_CODES.TIRAMISU)
    public ApplicationInfo getApplicationInfo(String packageName, int flags, int userId) throws RemoteException {
        return pm.getApplicationInfo(packageName, flags, userId);
    }

    @SuppressLint("DeprecatedSinceApi")
    public ApplicationInfo getApplicationInfo(String packageName, long flags, int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return getApplicationInfo(packageName, (int) flags, userId);
        }
        return pm.getApplicationInfo(packageName, flags, userId);
    }

    @SuppressLint("DeprecatedSinceApi")
    @DeprecatedSinceApi(api = Build.VERSION_CODES.TIRAMISU)
    public ParceledListSlice<ApplicationInfo> getInstalledApplications(int flags, int userId) throws RemoteException {
        return pm.getInstalledApplications(flags, userId);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("DeprecatedSinceApi")
    public ParceledListSlice<ApplicationInfo> getInstalledApplications(long flags, int userId) throws RemoteException {
        return pm.getInstalledApplications(flags, userId);
    }

    @SuppressLint("DeprecatedSinceApi")
    public List<ApplicationInfo> getInstalledApplicationList(long flags, int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return getInstalledApplications((int) flags, userId).getList();
        }
        return getInstalledApplications(flags, userId).getList();
    }

    @SuppressLint("DeprecatedSinceApi")
    @Override
    public ParceledListSlice<PackageInfo> getInstalledPackages(int flags, int userId) throws RemoteException {
        return pm.getInstalledPackages(flags, userId);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public ParceledListSlice<PackageInfo> getInstalledPackages(long flags, int userId) throws RemoteException {
        return pm.getInstalledPackages(flags, userId);
    }

    @SuppressLint("DeprecatedSinceApi")
    public List<PackageInfo> getInstalledPackageList(long flags, int userId) throws RemoteException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return getInstalledPackages((int) flags, userId).getList();
        }
        return getInstalledPackages(flags, userId).getList();
    }

    @SuppressLint("DeprecatedSinceApi")
    @DeprecatedSinceApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public PackageInfo getPackageInfo(String packageName, int flags, int userId) throws RemoteException {
        return pm.getPackageInfo(packageName, flags, userId);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public PackageInfo getPackageInfo(String packageName, long flags, int userId) throws RemoteException {
        return pm.getPackageInfo(packageName, flags, userId);
    }

    public boolean isPackageAvailable(String packageName, int userId) throws RemoteException {
        return pm.isPackageAvailable(packageName, userId);
    }

    public String getNameForUid(int uid) throws RemoteException {
        return pm.getNameForUid(uid);
    }

    @Override
    public void grantRuntimePermission(String packageName, String permissionName, int userId) throws RemoteException {
        pm.grantRuntimePermission(packageName, permissionName, userId);
    }

}
