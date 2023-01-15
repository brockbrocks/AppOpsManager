package com.android.internal.app;

import android.app.AppOpsManager;
import android.os.RemoteException;

import java.util.List;

public interface IAppOpsService {
    List<?> getPackagesForOps(int[] ops) throws RemoteException;
}
