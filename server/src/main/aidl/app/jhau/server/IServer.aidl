package app.jhau.server;
import app.jhau.server.IServerActivatedObserver;
import app.jhau.framework.appops.AppOpsManagerHidden;
import app.jhau.framework.pms.PackageManagerHidden;

interface IServer {
    //List<PackageInfo> getInstalledPackageInfoList(long flags);
    //List<ApplicationInfo> getInstalledApplicationList();
    void registerServerActivatedObserverOnce(IServerActivatedObserver observer);
    String execCommand(String cmd);
    void killServer();
    AppOpsManagerHidden getAppOpsManagerHidden();
    PackageManagerHidden getPackageManagerHidden();
    //void grantRuntimePermission(String packageName, String permissionName, int userId);
}