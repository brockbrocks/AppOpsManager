package app.jhau.server;

import android.app.ActivityManagerApi;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManagerApi;
import android.os.Build;
import android.os.Looper;
import android.os.RemoteException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import app.jhau.server.util.BinderSender;
import app.jhau.server.util.Constants;

public class AppOpsServer {

    private final AppOpsServerThread appOpsServerThread = new AppOpsServerThread();

    private void init() throws Throwable {
        ApplicationInfo appInfo = getApplicationInfo();
        //ActivityManagerApi.registerUidObserver(new IUidObserverImpl());
        ActivityManagerApi.registerProcessObserver(new IProcessObserverImpl(appInfo.uid, appOpsServerThread));
        BinderSender.sendBinder(appOpsServerThread);
    }

    private ApplicationInfo getApplicationInfo() throws Throwable {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0, 0);
        } else {
            return PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0L, 0);
        }
    }

    private static class AppOpsServerThread extends IAppOpsServer.Stub {

        @Override
        public String execCommand(String cmd) throws RemoteException {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));
                StringBuilder retBuilder = new StringBuilder();
                String tmp;
                while ((tmp = br.readLine()) != null) retBuilder.append(tmp).append("\n");
                br.close();
                return retBuilder.toString();
            } catch (Throwable e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public void killServer() throws RemoteException {
            try {
                BinderSender.sendBinder(null);
                android.os.Process.killProcess(android.os.Process.myPid());
            } catch (Throwable e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public List<ApplicationInfo> getInstalledApplications() throws RemoteException {
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    return PackageManagerApi.getInstalledApplications(0, 0);
                }
                return PackageManagerApi.getInstalledApplications(0L, 0);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public List<PackageInfo> getInstalledPackages() throws RemoteException {
            try {
                return PackageManagerApi.getInstalledPackages(0, 0);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RemoteException(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        Looper.prepare();
        new AppOpsServer().init();
        Looper.loop();
    }

}