package com.android.internal.app;

import android.app.AppOpsManager$PackageOps;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

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

    List<AppOpsManager$PackageOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException;
}
