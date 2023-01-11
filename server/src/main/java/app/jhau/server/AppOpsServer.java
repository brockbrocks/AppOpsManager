package app.jhau.server;

import android.annotation.SuppressLint;
import android.app.IProcessObserver;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

import app.jhau.framework_api.ActivityManagerApi;
import app.jhau.framework_api.PackageManagerApi;
import app.jhau.server.util.Constants;
import app.jhau.server.util.ServerProviderUtil;

public class AppOpsServer {
    private static final String TAG = Constants.DEBUG_TAG;

    private final AppOpsServerThread mServerThread = new AppOpsServerThread();
    private int userId;
    private int appUid;
    private String apkPath;

    private ApkFileObserver apkObserver;
    private IProcessObserver iProcessObserver = new IProcessObserver.Stub() {

        @Override
        public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) throws RemoteException {
            try {
                if (uid == appUid && foregroundActivities) {
                    sendServerBinderToApplication(mServerThread);
                }
            } catch (Throwable e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public void onProcessStateChanged(int pid, int uid, int procState) throws RemoteException {

        }

        @Override
        public void onProcessDied(int pid, int uid) throws RemoteException {

        }

        @Override
        public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) throws RemoteException {

        }
    };

    private void run() throws Throwable {
        Looper.prepare();
        userId = getUserId();
        ApplicationInfo appInfo = getApplicationInfo();
        appUid = appInfo.uid;
        apkPath = appInfo.sourceDir.substring(0, appInfo.sourceDir.lastIndexOf(File.separator));
        apkObserver = new ApkFileObserver.Build()
                .setCallback(() -> {
                    try {
                        boolean isPackageAvailable = PackageManagerApi.isPackageAvailable(Constants.APPLICATION_ID, userId);
                        Log.i(TAG, "isPackageAvailable=" + isPackageAvailable);
                        if (!isPackageAvailable) {
                            Log.i(TAG, "AppOpsServer exit.");
                            System.exit(0);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                }).filePath(apkPath).build();
        apkObserver.startWatching();
        registerProcessObserver(iProcessObserver);
        sendServerBinderToApplication(mServerThread);
        mServerThread.onActivated();
        Looper.loop();
    }

    private ApplicationInfo getApplicationInfo() throws Throwable {
        return PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0L, 0);
    }


    @SuppressLint("UnsafeDynamicallyLoadedCode")
    public static class AppOpsServerThread extends IAppOpsServer.Stub {
        private IServerActivatedObserver serverActivatedObserver;

        static {
            String classPath = System.getProperty("java.class.path");
            String libPath = classPath + "!/lib/" + Build.SUPPORTED_ABIS[0] + "/libserver.so";
            System.load(libPath);
        }

        public native String getCmdlineByPid(int pid);

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
                System.exit(0);
            } catch (Throwable e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public List<ApplicationInfo> getInstalledApplications() throws RemoteException {
            try {
                return PackageManagerApi.getInstalledApplications(0L, 0);
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public void registerServerActivatedObserverOnce(IServerActivatedObserver observer) throws RemoteException {
            serverActivatedObserver = observer;
        }

        public void onActivated() throws Throwable {
            if (serverActivatedObserver != null) {
                serverActivatedObserver.onActivated();
                serverActivatedObserver = null;
            }
        }

        public int getUserId() {
            return 0;
        }

    }

    public void sendServerBinderToApplication(AppOpsServerThread serverThread) throws Throwable {
        ServerProviderUtil.sendServerBinderToApplication(serverThread, getUserId());
    }

    public void registerProcessObserver(IProcessObserver iProcessObserver) throws Throwable {
        ActivityManagerApi.registerProcessObserver(iProcessObserver);
    }

    private int getUserId() {
        return 0;
    }

    public static void main(String[] args) throws Throwable {
        Log.i("AppOpsServer", "AppOpsServer start.");
        new AppOpsServer().run();
    }

}