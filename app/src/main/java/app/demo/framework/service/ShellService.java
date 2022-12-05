package app.demo.framework.service;

import android.app.ActivityManagerApi;
import android.app.ContentProviderHolder;
import android.app.IContentProviderHolderApi;
import android.content.IContentProvider;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;

import java.util.concurrent.TimeUnit;

import app.demo.framework.util.Constants;

public class ShellService extends IAdd.Stub {
    private static final String TAG = "ShellService";
    private static final int version = 30;
    private static final String serviceName = "shell_service";
    private static final String serviceToken = Constants.BINDER_SERVICE_NAME;

    public static void main(String[] args) {
        System.out.print("version=" + version);
        System.out.println(", pid=" + getCallingPid());
        try {
            Looper.prepare();
            int ret = addService(serviceToken);
            if (ret == 1) {
                System.out.println("服务启动时失败");
            } else if (ret == 0) {
                System.out.println("服务启动时成功");
            } else System.out.println("未知错误");
            Looper.loop();
        } catch (Throwable e) {
            e.printStackTrace();
        }
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
            IAdd add = IAdd.Stub.asInterface(binder);
            while (true) {
                System.out.println(add.add(1, 6));
                TimeUnit.SECONDS.sleep(2);
            }
        } catch (DeadObjectException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

    static {
        System.loadLibrary("binder_api");
    }

    private static native int addService(String serviceName);
}
