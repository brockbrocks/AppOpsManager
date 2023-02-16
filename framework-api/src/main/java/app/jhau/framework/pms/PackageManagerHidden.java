package app.jhau.framework.pms;

import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.List;

public class PackageManagerHidden extends IPackageManagerHidden.Stub implements Parcelable {
    private IPackageManagerHidden mRemote;
    private IPackageManager pm;
    private boolean isProxy = false;

    public List<PackageInfo> getInstalledPackages(int flags, int userId) throws RemoteException {
        if (isProxy) {
            return mRemote.getInstalledPackages(flags, userId);
        }
        return pm.getInstalledPackages(flags, userId).getList();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public List<PackageInfo> getInstalledPackagesApi33(long flags, int userId) throws RemoteException {
        if (isProxy) {
            return mRemote.getInstalledPackagesApi33(flags, userId);
        }
        return pm.getInstalledPackages(flags, userId).getList();
    }

    public PackageManagerHidden() {
        IBinder binder = ServiceManager.getService("package");
        pm = IPackageManager.Stub.asInterface(binder);
    }

    protected PackageManagerHidden(Parcel in) {
        isProxy = true;
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
}
