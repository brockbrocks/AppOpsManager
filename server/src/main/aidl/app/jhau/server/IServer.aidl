package app.jhau.server;
import app.jhau.server.IServerActivatedObserver;
import app.jhau.framework.appops.AppOpsManagerHidden;
import app.jhau.framework.pms.PackageManagerHidden;
import app.jhau.framework.permission.PermissionManagerHidden;

interface IServer {
    void registerServerActivatedObserverOnce(IServerActivatedObserver observer);
    String execCommand(String cmd);
    void killServer();
    AppOpsManagerHidden getAppOpsManagerHidden();
    PackageManagerHidden getPackageManagerHidden();
    PermissionManagerHidden getPermissionManagerHidden();
}