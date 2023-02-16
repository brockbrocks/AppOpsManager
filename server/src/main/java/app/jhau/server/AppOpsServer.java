package app.jhau.server;

import android.annotation.SuppressLint;
import android.app.IProcessObserver;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.UserInfo;
import android.os.Build;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

import app.jhau.framework.ams.ActivityManagerApi;
import app.jhau.framework.appops.AppOpsManagerHidden;
import app.jhau.framework.pms.PackageManagerApi;
import app.jhau.framework.pms.PackageManagerHidden;
import app.jhau.server.util.Constants;
import app.jhau.server.util.ServerProviderUtil;

public class AppOpsServer {
    private static final String TAG = Constants.DEBUG_TAG;
    private final IServerThread mServerThread = new IServerThread();
    private int userId;
    private int appUid;
    private String apkPath;
    private final AppObserver appObserver = new AppObserver();
    private final IProcessObserver iProcessObserver = new IProcessObserver.Stub() {

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
        updateAppInfo();
        registerAppObserver();
        registerProcessObserver(iProcessObserver);
        sendServerBinderToApplication(mServerThread);
        mServerThread.onActivated();
        Looper.loop();
    }

    private void updateAppInfo() {
        userId = getCurrentUserId();
        ApplicationInfo appInfo = getApplicationInfo(userId);
        appUid = appInfo != null ? appInfo.uid : -1;
        apkPath = resolveApkPath(appInfo);
    }

    private String resolveApkPath(ApplicationInfo appInfo) {
        if (appInfo == null) return "";
        String sourceDir = appInfo.sourceDir;
        return sourceDir.substring(0, sourceDir.lastIndexOf(File.separator));
    }

    private void registerAppObserver() {
        appObserver.observe(apkPath, userId, event -> {
            switch (event) {
                case REMOVE:
                    killServer();
                    break;
                case UPDATE:
                    updateAppInfo();
                    registerAppObserver();
                    break;
                default:
                    break;
            }
        });
    }

    private void killServer() {
        appObserver.stopObserve();
        Log.i(TAG, "AppOpsServer exit.");
        System.exit(0);
    }

    private void unRegisterAppObserver() {
        appObserver.stopObserve();
    }

    private ApplicationInfo getApplicationInfo(int userId) {
        try {
            return PackageManagerApi.getInstance().getApplicationInfo(Constants.APPLICATION_ID, 0L, userId);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendServerBinderToApplication(IServerThread serverThread) throws Throwable {
        ServerProviderUtil.sendServerBinderToApplication(serverThread, getCurrentUserId());
    }

    public void registerProcessObserver(IProcessObserver iProcessObserver) throws Throwable {
        ActivityManagerApi.registerProcessObserver(iProcessObserver);
    }

    private int getCurrentUserId() {
        UserInfo ui = ActivityManagerApi.getCurrentUser();
        return ui != null ? ui.id : 0;
    }

    public static void main(String[] args) throws Throwable {
        Log.i("AppOpsServer", "AppOpsServer start.");
        new AppOpsServer().run();
        Log.i("AppOpsServer", "!!!AppOpsServer error!!!");
    }

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    public static class IServerThread extends IServer.Stub {
        private IServerActivatedObserver serverActivatedObserver;
        private PackageManagerHidden packageManagerHidden;
        private AppOpsManagerHidden appOpsManagerHidden;

        static {
            String classPath = System.getProperty("java.class.path");
            String libPath = classPath + "!/lib/" + Build.SUPPORTED_ABIS[0] + "/libserver.so";
            System.load(libPath);
        }

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
        public AppOpsManagerHidden getAppOpsManagerHidden() throws RemoteException {
            try {
                if (appOpsManagerHidden == null) appOpsManagerHidden = new AppOpsManagerHidden();
                return appOpsManagerHidden;
            } catch (Throwable e) {
                throw new RemoteException(e.getMessage());
            }
        }

        @Override
        public PackageManagerHidden getPackageManagerHidden() throws RemoteException {
            try {
                if (packageManagerHidden == null) packageManagerHidden = new PackageManagerHidden();
                return packageManagerHidden;
            } catch (Throwable e) {
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
    }

}