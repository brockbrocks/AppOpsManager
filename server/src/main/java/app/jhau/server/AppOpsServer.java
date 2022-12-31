package app.jhau.server;

import android.app.ActivityManagerApi;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManagerApi;
import android.os.Build;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.InputStreamReader;
import java.util.List;

import app.jhau.server.util.BinderSender;
import app.jhau.server.util.Constants;

public class AppOpsServer {
    private static String TAG = Constants.DEBUG_TAG;

    private final AppOpsServerThread appOpsServerThread = new AppOpsServerThread();
    ApkObserverCompat apkObserver;

    private void run() throws Throwable {
        Looper.prepare();
        ApplicationInfo appInfo = getApplicationInfo();
        ActivityManagerApi.registerProcessObserver(new IProcessObserverImpl(appInfo.uid, appOpsServerThread));
        String sourceDir = appInfo.sourceDir;
        String apkPath = sourceDir.substring(0, sourceDir.lastIndexOf(File.separator));
        apkObserver = new ApkObserverCompat.Build()
                .setCallback(() -> {
                    Log.i(TAG, "AppOpsServer exit.");
                    System.exit(0);
                }).filePath(apkPath).build();
        apkObserver.startWatching();
        BinderSender.sendBinder(appOpsServerThread);
        Looper.loop();
    }

    private ApplicationInfo getApplicationInfo() throws Throwable {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0, 0);
        } else {
            return PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0L, 0);
        }
    }

    public static class AppOpsServerThread extends IAppOpsServer.Stub {

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
                System.exit(0);
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

        static {
            String classPath = System.getProperty("java.class.path");
            String libPath = classPath + "!/lib/" + Build.SUPPORTED_ABIS[0] + "/libserver.so";
            System.load(libPath);
        }

        public native String getCmdlineByPid(int pid);
    }

    public static void main(String[] args) throws Throwable {
        Log.i("AppOpsServer", "AppOpsServer start.");
        new AppOpsServer().run();
    }

}