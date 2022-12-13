package app.jhau.server.util;

import android.content.IContentProvider;

import java.lang.reflect.Field;

public class ContentProviderHolderWrapper {

    private final Object holder;

    public ContentProviderHolderWrapper(Object holder) {
        this.holder = holder;
    }

    public IContentProvider getProvider() throws Throwable {
        try {
            Field providerField = holder.getClass().getDeclaredField("provider");
            providerField.setAccessible(true);
            return (IContentProvider) providerField.get(holder);
        } catch (Throwable e) {
            throw e;
        }
    }
}
