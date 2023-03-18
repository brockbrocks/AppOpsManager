package com.android.internal.app;

import android.app.AppOpsManager$PackageOps;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

public interface IAppOpsService extends IInterface {

    abstract class Stub extends Binder implements IAppOpsService {

        public static IAppOpsService asInterface(IBinder obj) {
            return null;
        }

        @Override
        public IBinder asBinder() {
            return null;
        }

    }

    @Override
    default IBinder asBinder() {
        return null;
    }

    void setUidMode(int code, int uid, int mode) throws RemoteException;

    void setMode(int code, int uid, String packageName, int mode) throws RemoteException;

    @RequiresApi(Build.VERSION_CODES.O)
    List<AppOpsManager$PackageOps> getUidOps(int uid, @Nullable int[] ops);

    List<AppOpsManager$PackageOps> getOpsForPackage(int uid, String packageName, @Nullable int[] ops) throws RemoteException;

    List<AppOpsManager$PackageOps> getOpsForPackage(int uid, @NonNull String packageName, @Nullable String[] ops) throws RemoteException;
}
