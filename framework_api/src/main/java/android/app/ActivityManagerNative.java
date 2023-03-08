package android.app;

import android.os.Build;
import android.os.IBinder;

import androidx.annotation.DeprecatedSinceApi;

@DeprecatedSinceApi(api = Build.VERSION_CODES.O)
public class ActivityManagerNative {
    static public IActivityManager getDefault() {
        return null;
    }

    static public IActivityManager asInterface(IBinder obj) {
        return null;
    }
}
