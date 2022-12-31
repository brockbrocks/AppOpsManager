// IAppOpsServer.aidl
package app.jhau.server;

interface IAppOpsServer {
    String execCommand(String cmd);
    void killServer();
    List<ApplicationInfo> getInstalledApplications();
}