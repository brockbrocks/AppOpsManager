package app.jhau.framework.appops;

import android.app.AppOpsManager$OpEntry;
import android.app.AppOpsManager$PackageOps;
import android.os.RemoteException;

import java.util.List;

public class AppOpsMgr23 extends AppOpsMgrBase {
    @Override
    public List<AbsPkgOps> getOpsForPackage(int uid, String packageName, int[] ops) throws RemoteException {
        return super.getOpsForPackage(uid, packageName, ops);
    }

    public static class PkgOps23 extends AppOpsMgrBase.PkgOpsBase {

        public PkgOps23(AppOpsManager$PackageOps ops) {
            super(ops);
        }
    }

    public static class OpEntry23 extends OpEntryBase {
        public OpEntry23(AppOpsManager$OpEntry opEntry) {
            super(opEntry);
        }
    }
}
