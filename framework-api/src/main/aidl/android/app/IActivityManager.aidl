package android.app;

import android.app.IApplicationThread;
import android.app.ContentProviderHolder;
import android.app.IUidObserver;
import android.app.IProcessObserver;
import java.util.List;

interface IActivityManager {
    void registerUidObserver(in IUidObserver observer, int which, int cutpoint, String callingPackage);
    void unregisterUidObserver(in IUidObserver observer);
    void registerProcessObserver(in IProcessObserver observer);
    void unregisterProcessObserver(in IProcessObserver observer);
    ContentProviderHolder getContentProviderExternal(in String name, int userId, in IBinder token, String tag);
}