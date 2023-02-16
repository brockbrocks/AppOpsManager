package app.jhau.framework.appops;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.AppOpsManager$PackageOps;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;

import com.android.internal.app.IAppOpsService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class AppOpsManagerImpl extends IAppOpsManager.Stub {
    private final IAppOpsService iAppOpsService;

    AppOpsManagerImpl() {
        iAppOpsService = IAppOpsService.Stub.asInterface(ServiceManager.getService(Context.APP_OPS_SERVICE));
    }

    private Object reflectField(String name, Object obj) throws Throwable {
        Field field = AppOpsManager.class.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(obj);
    }

    private Method reflectMethod(String name, Class<?>... args) throws Throwable {
        Method method = AppOpsManager.class.getDeclaredMethod(name, args);
        method.setAccessible(true);
        return method;
    }

    @Override
    public String[] getModeNames() throws RemoteException {
        try {
            String[] modeNames = (String[]) reflectField("MODE_NAMES", null);
            return modeNames;
        } catch (Throwable e) {
            throw new RemoteException(e.getMessage());
        }
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
