package app.jhau.server;
import app.jhau.server.IServerActivatedObserver;

interface IAppOpsServer {
    List<PackageInfo> getInstalledPackageInfoList();
    List<ApplicationInfo> getInstalledApplicationList();
    void registerServerActivatedObserverOnce(IServerActivatedObserver observer);
    String execCommand(String cmd);
    void killServer();
}