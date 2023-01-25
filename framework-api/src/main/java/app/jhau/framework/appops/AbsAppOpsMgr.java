package app.jhau.framework.appops;

import android.os.Parcelable;
import android.os.RemoteException;

import java.util.List;

public interface AbsAppOpsMgr {

    int getNumOp();
    List<AbsAppOpsMgr.AbsPkgOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException;

    interface AbsPkgOps extends Parcelable {

        String getPackageName();

        int getUid();

        List<AbsAppOpsMgr.AbsOpEntry> getOps();
    }

    interface AbsOpEntry extends Parcelable{
        int getOp();

        int getMode();

        long getTime();

        long getRejectTime();

        boolean isRunning();

        int getDuration();

        int getProxyUid();

        String getProxyPackageName();

    }
}
