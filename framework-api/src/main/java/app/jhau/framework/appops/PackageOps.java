package app.jhau.framework.appops;

import android.app.AppOpsManager$OpEntry;
import android.app.AppOpsManager$PackageOps;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

/**
 * PackageOps
 */
public final class PackageOps implements Parcelable {
    private final IPackageOps iPackageOps;

    public PackageOps(AppOpsManager$PackageOps pkgOp) {
        iPackageOps = new PackageOpsImpl(pkgOp);
    }

    protected PackageOps(Parcel in) {
        iPackageOps = IPackageOps.Stub.asInterface(in.readStrongBinder());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(iPackageOps.asBinder());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PackageOps> CREATOR = new Creator<PackageOps>() {
        @Override
        public PackageOps createFromParcel(Parcel in) {
            return new PackageOps(in);
        }

        @Override
        public PackageOps[] newArray(int size) {
            return new PackageOps[size];
        }
    };

    private static final class PackageOpsImpl extends IPackageOps.Stub {
        private AppOpsManager$PackageOps pkgOp;

        public PackageOpsImpl(AppOpsManager$PackageOps pkgOp) {
            this.pkgOp = pkgOp;
        }

        @Override
        public String getPackageName() throws RemoteException {
            return pkgOp.getPackageName();
        }

        @Override
        public int getUid() throws RemoteException {
            return pkgOp.getUid();
        }

        @Override
        public List<OpEntry> getOps() throws RemoteException {
            List<AppOpsManager$OpEntry> opEntries = pkgOp.getOps();
            List<OpEntry> ret = new ArrayList<>();
            for (AppOpsManager$OpEntry opEntry : opEntries) {
                ret.add(new OpEntry(opEntry));
            }
            return ret;
        }
    }

    public String getPackageName() throws RemoteException {
        return iPackageOps.getPackageName();
    }

    public int getUid() throws RemoteException {
        return iPackageOps.getUid();
    }

    public List<OpEntry> getOps() throws RemoteException {
        return iPackageOps.getOps();
    }
}
