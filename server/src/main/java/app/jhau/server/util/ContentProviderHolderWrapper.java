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
            Field field = holder.getClass().getDeclaredField("provider");
            field.setAccessible(true);
            return (IContentProvider) field.get(holder);
        } catch (Throwable e) {
            throw e;
        }
    }
}
