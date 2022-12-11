// IAppOpsServer.aidl
package app.jhau.server;

interface IAppOpsServer {
    String execCommand(String cmd);
    boolean isAlive();
}