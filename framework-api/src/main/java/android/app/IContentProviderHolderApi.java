package android.app;

import android.content.IContentProvider;

import java.lang.reflect.Field;

public class IContentProviderHolderApi {
    public static IContentProvider getProvider(ContentProviderHolder holder) {
        try {
            Field providerField = holder.getClass().getDeclaredField("provider");
            providerField.setAccessible(true);
            return (IContentProvider) providerField.get(holder);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
