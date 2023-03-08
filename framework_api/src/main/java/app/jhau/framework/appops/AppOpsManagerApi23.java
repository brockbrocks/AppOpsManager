package app.jhau.framework.appops;

import android.os.RemoteException;

public class AppOpsManagerApi23 extends AppOpsManagerHiddenImpl {
    public final int MODE_ALLOWED = 0;
    public final int MODE_IGNORED = 1;
    public final int MODE_ERRORED = 2;
    public final int MODE_DEFAULT = 3;

    @Override
    public String[] getModeNames() throws RemoteException {
        return MODE_NAMES;
    }

    @Override
    public String modeToName(int mode) throws RemoteException {
        if (mode >= 0 && mode < MODE_NAMES.length) {
            return MODE_NAMES[mode];
        }
        return "mode=" + mode;
    }

    public static final String[] MODE_NAMES = new String[] {
            "allow",        // MODE_ALLOWED
            "ignore",       // MODE_IGNORED
            "deny",         // MODE_ERRORED
            "default",      // MODE_DEFAULT
//            "foreground",   // MODE_FOREGROUND
    };
}
