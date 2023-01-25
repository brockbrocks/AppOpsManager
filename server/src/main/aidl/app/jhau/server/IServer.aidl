package app.jhau.server;
import app.jhau.server.IServerActivatedObserver;

interface IServer {
    List<PackageInfo> getInstalledPackageInfoList();
    List<ApplicationInfo> getInstalledApplicationList();
    List getOpsForPackage(int uid, String packageName);
    void registerServerActivatedObserverOnce(IServerActivatedObserver observer);
    String execCommand(String cmd);
    void killServer();
}