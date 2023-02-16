package app.jhau.framework.pms;

import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.List;

public class PackageManagerHidden extends IPackageManagerHidden.Stub implements Parcelable {
    private IPackageManagerHidden pmProxy;
    private IPackageManager pm;
    private boolean isProxy = false;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public List<PackageInfo> getInstalledPackages(long flags, int userId) throws RemoteException {
        if (isProxy) {
            return pmProxy.getInstalledPackages(flags, userId);
        }
        return pm.getInstalledPackages(flags, userId).getList();
    }

    public List<PackageInfo> getInstalledPackages(int flags, int userId) throws RemoteException {
        Log.i("tttt", "getInstalledPackages: uid=" + android.os.Process.myUid() + ", pid=" + android.os.Process.myPid());
        try {
            if (isProxy) {
                return pmProxy.getInstalledPackages(flags, userId);
            }
            return pm.getInstalledPackages(flags, userId).getList();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkBinder() {
        if (!pmProxy.asBinder().pingBinder()) {
            pm = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
            Log.i("tttt", "checkBinder: ");
        } else {
            Log.i("tttt", "checkBinder: false");
        }
    }

    public PackageManagerHidden() {
        IBinder binder = ServiceManager.getService("package");
        pm = IPackageManager.Stub.asInterface(binder);
        Binder.joinThreadPool();
    }

    protected PackageManagerHidden(Parcel in) {
        isProxy = true;
        pmProxy = IPackageManagerHidden.Stub.asInterface(in.readStrongBinder());
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
}
