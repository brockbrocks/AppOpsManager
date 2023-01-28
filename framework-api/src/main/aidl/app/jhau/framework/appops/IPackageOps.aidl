package app.jhau.framework.appops;
import app.jhau.framework.appops.OpEntry;

interface IPackageOps {
    String getPackageName();
    int getUid();
    List<OpEntry> getOps();
}