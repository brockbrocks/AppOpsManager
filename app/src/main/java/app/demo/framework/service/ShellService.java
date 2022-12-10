package app.demo.framework.service;

import android.app.ActivityManagerApi;
import android.app.ActivityThreadApi;
import android.app.ContentProviderHolder;
import android.app.IContentProviderHolderApi;
import android.content.IContentProvider;
import android.content.pm.IPackageManager;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;

import java.util.concurrent.TimeUnit;

import app.demo.framework.util.Constants;

public class ShellService extends IShellServiceManager.Stub {
    private static final String TAG = "ShellService";
    private static final int version = 30;
    private static final String serviceName = "shell_service";
    private static final String serviceToken = Constants.BINDER_SERVICE_NAME;

    public static void main(String[] args) {
        System.out.print("version=" + version + ", pid=" + getCallingPid());
        try {
            Looper.prepare();
            initServer();
            Looper.loop();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void initServer() {
        IPackageManager pm = ActivityThreadApi.getPackageManager();
    }

    //实验成功，能够成功获取ContentProvider Binder对象
    private static void getContentProvider() {

        final String name = "app.demo.framework.testprovider";
        final IBinder token = null;
        final int userId = 0;
        final String tag = null;
        try {
            ContentProviderHolder holder = ActivityManagerApi.getService().getContentProviderExternal(name, userId, token, tag);
            IContentProvider provider = IContentProviderHolderApi.getProvider(holder);
            Bundle bundle = provider.call("", "app.demo.framework.testprovider", "", "", new Bundle());
            IBinder binder = bundle.getBinder("binder");

        } catch (DeadObjectException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static {
        System.loadLibrary("binder_api");
    }

    private static native int addService(String serviceName);

    @Override
    public String execCommand(String cmd) throws RemoteException {
        return "test";
    }
}
