package app.jhau.framework.appops;

import android.app.AppOpsManager;
import android.app.AppOpsManager$PackageOps;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;

import androidx.annotation.NonNull;

import com.android.internal.app.IAppOpsService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.jhau.framework.util.ReflectUtil;

public class AppOpsManagerHidden extends IAppOpsManagerHidden.Stub implements Parcelable {
    private IAppOpsService appOpsSvc;
    private IAppOpsManagerHidden mRemote;

    public AppOpsManagerHidden() {
        appOpsSvc = IAppOpsService.Stub.asInterface(ServiceManager.getService(Context.APP_OPS_SERVICE));
        initFields();
    }

    AppOpsManagerHidden(Parcel in) {
        mRemote = IAppOpsManagerHidden.Stub.asInterface(in.readStrongBinder());
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
    public List<PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
        if (mRemote != null) return mRemote.getOpsForPackage(uid, packageName, ops);

        List<AppOpsManager$PackageOps> pkgOpsList = appOpsSvc.getOpsForPackage(uid, packageName, ops);
        if (pkgOpsList == null || pkgOpsList.isEmpty()) return Collections.emptyList();

        List<PackageOps> ret = new ArrayList<>();
        for (AppOpsManager$PackageOps pkgOps : pkgOpsList) {
            ret.add(new PackageOps(pkgOps));
        }
        return ret;
    }

    @Override
    public void setMode(int code, int uid, String packageName, int mode) throws RemoteException {
        if (mRemote != null) {
            mRemote.setMode(code, uid, packageName, mode);
        } else {
            appOpsSvc.setMode(code, uid, packageName, mode);
        }
    }

    @Override
    public void setUidMode(int code, int uid, int mode) throws RemoteException {
        if (mRemote != null) {
            mRemote.setUidMode(code, uid, mode);
        } else {
            appOpsSvc.setUidMode(code, uid, mode);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeStrongBinder(this.asBinder());
    }

    @Override
    public String modeToName(int mode) throws RemoteException {
        if (mRemote != null) return mRemote.modeToName(mode);
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return MODE_NAMES[mode];
            return (String) ReflectUtil.invokeStaticMethod(AppOpsManager.class, "modeToName", int.class);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String[] getOpNames() throws RemoteException {
        if (mRemote != null) return mRemote.getOpNames();
        return sOpNames;
    }

    @Override
    public String[] getModeNames() throws RemoteException {
        if (mRemote != null) return mRemote.getModeNames();
        return MODE_NAMES;
    }

    public String[] sOpNames;

    public String[] MODE_NAMES;

    private void initFields() {
        sOpNames = ReflectUtil.getStaticField(AppOpsManager.class, "sOpNames");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            MODE_NAMES = new String[]{
                    "allow",        // MODE_ALLOWED
                    "ignore",       // MODE_IGNORED
                    "deny",         // MODE_ERRORED
                    "default"      // MODE_DEFAULT
            };
        } else {
            MODE_NAMES = ReflectUtil.getStaticField(AppOpsManager.class, "MODE_NAMES");
        }
    }

}
