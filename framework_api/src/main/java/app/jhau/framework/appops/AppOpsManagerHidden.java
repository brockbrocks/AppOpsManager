package app.jhau.framework.appops;

import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class AppOpsManagerHidden implements IAppOpsManagerHidden, Parcelable {
    private final IAppOpsManagerHidden iAppOpsManager;

    public AppOpsManagerHidden() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            iAppOpsManager = new AppOpsManagerHiddenImpl();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            iAppOpsManager = new AppOpsManagerApi28();
        } else {
            iAppOpsManager = new AppOpsManagerApi23();
        }
    }

    protected AppOpsManagerHidden(Parcel in) {
        iAppOpsManager = IAppOpsManagerHidden.Stub.asInterface(in.readStrongBinder());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(iAppOpsManager.asBinder());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppOpsManagerHidden> CREATOR = new Creator<AppOpsManagerHidden>() {
        @Override
        public AppOpsManagerHidden createFromParcel(Parcel in) {
            return new AppOpsManagerHidden(in);
        }

        @Override
        public AppOpsManagerHidden[] newArray(int size) {
            return new AppOpsManagerHidden[size];
        }
    };

    @Override
    public IBinder asBinder() {
        return null;
    }

    @Override
    public String[] getModeNames() throws RemoteException {
        return iAppOpsManager.getModeNames();
    }

    @Override
    public String modeToName(int mode) throws RemoteException {
        return iAppOpsManager.modeToName(mode);
    }

    //@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R)
    @Override
    public String opToName(int op) throws RemoteException {
        return iAppOpsManager.opToName(op);
    }

    @Override
    public void setUidMode(int code, int uid, int mode) throws RemoteException {
        iAppOpsManager.setUidMode(code, uid, mode);
    }

    @Override
    public void setMode(int code, int uid, String packageName, int mode) throws RemoteException {
        iAppOpsManager.setMode(code, uid, packageName, mode);
    }

    @Override
    public List<PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
        List<PackageOps> ret = iAppOpsManager.getOpsForPackage(uid, packageName, ops);
        if (ret == null) return new ArrayList<>();
        return ret;
    }

}
