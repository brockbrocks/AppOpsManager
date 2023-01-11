package app.jhau.server;
import app.jhau.server.IServerActivatedObserver;

interface IAppOpsServer {
    String execCommand(String cmd);
    void killServer();
    List<ApplicationInfo> getInstalledApplications();
    void registerServerActivatedObserverOnce(IServerActivatedObserver observer);
}