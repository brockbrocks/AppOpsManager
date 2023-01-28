package app.jhau.framework.ams;

import android.content.IContentProvider;
import android.os.Build;

public class ContentProviderHolderWrapper {
    public IContentProvider provider;

    public ContentProviderHolderWrapper(Object holder) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            provider = ((android.app.IActivityManager.ContentProviderHolder) holder).provider;
        } else {
            provider = ((android.app.ContentProviderHolder) holder).provider;
        }
    }
}
