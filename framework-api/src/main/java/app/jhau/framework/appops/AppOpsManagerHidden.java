package app.jhau.framework.appops;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.AppOpsManager$PackageOps;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.android.internal.app.IAppOpsService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AppOpsManagerHidden implements Parcelable {
    private final IAppOpsManager iAppOpsManager;

    public AppOpsManagerHidden() {
        iAppOpsManager = new AppOpsManagerImpl();
    }

    protected AppOpsManagerHidden(Parcel in) {
        iAppOpsManager = IAppOpsManager.Stub.asInterface(in.readStrongBinder());
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

    private static final class AppOpsManagerImpl extends IAppOpsManager.Stub {
        private final IAppOpsService iAppOpsService;

        AppOpsManagerImpl() {
            iAppOpsService = IAppOpsService.Stub.asInterface(ServiceManager.getService(Context.APP_OPS_SERVICE));
        }

        private Method reflectMethod(String name, Class<?>... args) throws Throwable {
            Method method = AppOpsManager.class.getDeclaredMethod(name, args);
            return method;
        }

        @Override
        public String modeToName(int mode) throws RemoteException {
            try {
                Method method = reflectMethod("modeToName", int.class);
                return (String) method.invoke(null, mode);
            } catch (Throwable e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @SuppressLint("SoonBlockedPrivateApi")
        @Override
        public String opToName(int op) throws RemoteException {
            try {
                Method method = AppOpsManager.class.getDeclaredMethod("opToName", int.class);
                return (String) method.invoke(null, op);
            } catch (Throwable e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public void setUidMode(int code, int uid, int mode) throws RemoteException {
            iAppOpsService.setUidMode(code, uid, mode);
        }

        @Override
        public void setMode(int code, int uid, String packageName, int mode) throws RemoteException {
            iAppOpsService.setMode(code, uid, packageName, mode);
        }

        @Override
        public List<PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
            List<AppOpsManager$PackageOps> pkgOps = iAppOpsService.getOpsForPackage(uid, packageName, ops);
            List<PackageOps> ret = new ArrayList<>();
            if (pkgOps != null) {
                for (AppOpsManager$PackageOps pkgOp : pkgOps) {
                    ret.add(new PackageOps(pkgOp));
                }
            }
            return ret;
        }
    }

    public String modeToName(int mode) throws RemoteException {
        return iAppOpsManager.modeToName(mode);
    }

    //@UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R)
    public String opToName(int op) throws RemoteException {
        return iAppOpsManager.opToName(op);
    }

    public void setUidMode(int code, int uid, int mode) throws RemoteException {
        iAppOpsManager.setUidMode(code, uid, mode);
    }

    public void setMode(int code, int uid, String packageName, int mode) throws RemoteException {
        iAppOpsManager.setMode(code, uid, packageName, mode);
    }

    public List<PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
        return iAppOpsManager.getOpsForPackage(uid, packageName, ops);
    }

}
