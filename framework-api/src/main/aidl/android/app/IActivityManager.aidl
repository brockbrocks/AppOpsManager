package android.app;

import android.app.IApplicationThread;
import android.app.ContentProviderHolder;
import java.util.List;

interface IActivityManager {
    void publishContentProviders(in IApplicationThread caller, in List<ContentProviderHolder> providers);
    ContentProviderHolder getContentProvider(in IApplicationThread caller, in String callingPackage, in String name, int userId, boolean stable);
    ContentProviderHolder getContentProviderExternal(in String name, int userId, in IBinder token, String tag);
}