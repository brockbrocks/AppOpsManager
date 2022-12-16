package app.jhau.server;

import android.app.ActivityManagerApi;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManagerApi;
import android.os.Build;
import android.os.Looper;
import android.os.RemoteException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import app.jhau.server.util.BinderSender;
import app.jhau.server.util.Constants;

public class AppOpsServer extends IAppOpsServer.Stub {

    private static final String TAG = Constants.DEBUG_TAG;

    @Override
    public String execCommand(String cmd) throws RemoteException {
        InputStream is;
        Reader r;
        StringBuilder ret = new StringBuilder();
        try {
            is = Runtime.getRuntime().exec(cmd).getInputStream();
            r = new InputStreamReader(is);
            char[] buff = new char[4096];
            while (r.read(buff, 0, buff.length) != -1) {
                ret.append(buff, 0, buff.length);
            }
            try {
                r.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ret.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Throwable {
        //prepare
        Looper.prepare();
        AppOpsServer appOpsServer = new AppOpsServer();
        ApplicationInfo appInfo = appOpsServer.getApplicationInfo();
        ActivityManagerApi.registerProcessObserver(new IProcessObserverImpl(appInfo.uid, appOpsServer));
        BinderSender.sendBinder(appOpsServer);
        //start loop
        Looper.loop();
    }


    private ApplicationInfo getApplicationInfo() throws Throwable {
        ApplicationInfo appInfo;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0, 0);
        } else {
            appInfo = PackageManagerApi.getApplicationInfo(Constants.APPLICATION_ID, 0L, 0);
        }
        return appInfo;
    }

}