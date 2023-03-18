package app.jhau.framework.appops;

import app.jhau.framework.appops.PackageOps;

interface IAppOpsManagerHidden {
    List<PackageOps> getOpsForPackage(int uid, String packageName, in int[] ops);
    void setMode(int code, int uid, String packageName, int mode);
    void setUidMode(int code, int uid, int mode);

    String modeToName(int mode); //special
    String[] getOpNames(); //special
    String[] getOpStrs(); //special
    String[] getModeNames(); //special
}