package android.app;

import android.app.ContentProviderHolder;
import android.app.IProcessObserver;

interface IActivityManager {
    void registerProcessObserver(in IProcessObserver observer);
    void unregisterProcessObserver(in IProcessObserver observer);

    //Android Q and later
    ContentProviderHolder getContentProviderExternal(in String name, int userId, in IBinder token, String tag);

    //ContentProviderHolder getContentProviderExternal(in String name, int userId, in IBinder token);
}