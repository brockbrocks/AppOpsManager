package app.demo.framework.service;

import android.app.ActivityManagerApi;
import android.app.ActivityThread;
import android.app.ActivityThreadApi;
import android.app.ContentProviderHolder;
import android.app.IActivityManager;
import android.app.IApplicationThread;
import android.content.pm.IPackageManager;
import android.content.pm.ProviderInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;


public class ShellService extends IAdd.Stub {
    private static final String TAG = "ShellService";
    private static final int version = 30;
    private static final String serviceName = "shell_service";
    private static final String serviceToken = "app.demo.framework.shellService";


    public static void main(String[] args) {
        System.out.println("version=" + version);
//        System.out.println("pid=" + getCallingPid());
        try {
            Looper.prepare();
            initService();
            test();
            Looper.loop();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void test() {
        IPackageManager pm = ActivityThreadApi.getPackageManager();
        ProviderInfo providerInfo = null;
        try {
            providerInfo = pm.resolveContentProvider("app.demo.framework.testprovider", 1024 | 2048, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        IActivityManager am = ActivityManagerApi.getService();
        ContentProviderHolder holder = null;
        try {
            IApplicationThread caller = ActivityThreadApi.getApplicationThread(mThread);
            holder = am.getContentProvider(null, null, "testprovider", 0, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println(providerInfo.applicationInfo.uid);
    }

    private static ActivityThread mThread;

    private static void initService() throws Exception {
        mThread = ActivityThreadApi.systemMain();
//        Handler handler = ActivityThreadApi.get_mH(thread);
//        installContentProvider(handler);
    }

    private static void installContentProvider(Handler handler) {

    }


    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

}
