package app.jhau.framework.appops;

import android.app.AppOpsManager;
import android.app.AppOpsManager$PackageOps;
import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.internal.app.IAppOpsService;

import java.lang.reflect.Method;
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

    @RequiresApi(Build.VERSION_CODES.O)
    @Override
    public List<PackageOps> getUidOps(int uid, @Nullable int[] ops) throws RemoteException {
        if (mRemote != null) return mRemote.getUidOps(uid, ops);
        try {
            //
            // Before Android Q, there will be some weird bugs caused by remote AppOpsService.
            // So I embed it inside a try-catch statement
            //
            List<AppOpsManager$PackageOps> pkgOpsList = appOpsSvc.getUidOps(uid, ops);
            if (pkgOpsList == null) return Collections.emptyList();

            List<PackageOps> ret = new ArrayList<>();
            for (AppOpsManager$PackageOps pkgOps : pkgOpsList) {
                ret.add(new PackageOps(pkgOps));
            }
            return ret;
        } catch (Throwable e) {
            return Collections.emptyList();
        }
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
    public String[] getOpStrs() throws RemoteException {
        if (mRemote != null) return mRemote.getOpStrs();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return (String[]) ReflectUtil.invokeStaticMethod(AppOpsManager.class, "getOpStrs");
            } else {
                return ReflectUtil.getStaticField(AppOpsManager.class, "sOpToString");
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
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

    /**
     * Flag: non proxy operations. These are operations
     * performed on behalf of the app itself and not on behalf of
     * another one.
     */
    public static final int OP_FLAG_SELF = 0x1;
    /**
     * Flag: trusted proxy operations. These are operations
     * performed on behalf of another app by a trusted app.
     * Which is work a trusted app blames on another app.
     */
    public static final int OP_FLAG_TRUSTED_PROXY = 0x2;
    /**
     * Flag: untrusted proxy operations. These are operations
     * performed on behalf of another app by an untrusted app.
     * Which is work an untrusted app blames on another app.
     */
    public static final int OP_FLAG_UNTRUSTED_PROXY = 0x4;
    /**
     * Flag: trusted proxied operations. These are operations
     * performed by a trusted other app on behalf of an app.
     * Which is work an app was blamed for by a trusted app.
     */
    public static final int OP_FLAG_TRUSTED_PROXIED = 0x8;
    /**
     * Flag: untrusted proxied operations. These are operations
     * performed by an untrusted other app on behalf of an app.
     * Which is work an app was blamed for by an untrusted app.
     */
    public static final int OP_FLAG_UNTRUSTED_PROXIED = 0x10;
    /**
     * Flags: all operations. These include operations matched
     * by {@link #OP_FLAG_SELF}, {@link #OP_FLAG_TRUSTED_PROXIED},
     * {@link #OP_FLAG_UNTRUSTED_PROXIED}, {@link #OP_FLAG_TRUSTED_PROXIED},
     * {@link #OP_FLAG_UNTRUSTED_PROXIED}.
     */
    public static final int OP_FLAGS_ALL = OP_FLAG_SELF | OP_FLAG_TRUSTED_PROXY
            | OP_FLAG_UNTRUSTED_PROXY | OP_FLAG_TRUSTED_PROXIED | OP_FLAG_UNTRUSTED_PROXIED;

    /**
     * Flags: all trusted operations which is ones either the app did {@link #OP_FLAG_SELF},
     * or it was blamed for by a trusted app {@link #OP_FLAG_TRUSTED_PROXIED}, or ones the
     * app if untrusted blamed on other apps {@link #OP_FLAG_UNTRUSTED_PROXY}.
     */
    public static final int OP_FLAGS_ALL_TRUSTED = OP_FLAG_SELF | OP_FLAG_UNTRUSTED_PROXY | OP_FLAG_TRUSTED_PROXIED;

}
