package app.jhau.framework.appops;

import android.app.AppOpsManager$OpEntry;
import android.app.AppOpsManager$PackageOps;

public class AppOpsMgr26 extends AppOpsMgrBase {

    public static class PkgOps26 extends PkgOpsBase {

        public PkgOps26(AppOpsManager$PackageOps ops) {
            super(ops);
        }
    }

    public static class OpEntry26 extends OpEntryBase {

        public OpEntry26(AppOpsManager$OpEntry opEntry) {
            super(opEntry);
        }
    }
}
