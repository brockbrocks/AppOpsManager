package app.jhau.framework.appops;
import app.jhau.framework.appops.PackageOps;

interface IAppOpsManager {
    //int getNumOps();
    String modeToName(int mode);
    String opToName(int op);
    void setUidMode(int code, int uid, int mode);
    void setMode(int code, int uid, String packageName, int mode);
    List<PackageOps> getOpsForPackage(int uid, String packageName, in int[] ops);
}