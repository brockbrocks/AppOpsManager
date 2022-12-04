package app.demo.framework.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class TestService extends Service {
    private static final String TAG = "TestService";
    private IShellServiceManager.Stub stub = new IShellServiceManager.Stub() {
        @Override
        public String execCommand(String cmd) throws RemoteException {
            return "test";
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
    }
}
