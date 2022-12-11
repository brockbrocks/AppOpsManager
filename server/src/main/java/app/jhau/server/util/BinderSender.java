package app.jhau.server.util;

import static app.jhau.server.provider.ServerProvider.SAVE_BINDER;

import android.app.ActivityManagerApi;
import android.app.ContentProviderHolder;
import android.app.IContentProviderHolderApi;
import android.content.IContentProvider;
import android.os.Bundle;
import android.os.IBinder;

import app.jhau.server.provider.ServerProvider;

public class BinderSender {
    public static void sendBinder(IBinder binder) {
        try {
            IContentProvider provider = getContentProvider();
            Bundle bundle = new Bundle();
            bundle.putBinder(Constants.SERVER_BINDER_KEY, binder);
            provider.call("", ServerProvider.AUTHORITY, SAVE_BINDER, "", bundle);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static IContentProvider getContentProvider() throws Throwable {
        final IBinder token = null;
        final int userId = 0;
        final String tag = null;
        ContentProviderHolder holder = ActivityManagerApi.getService().getContentProviderExternal(ServerProvider.AUTHORITY, userId, token, tag);
        IContentProvider provider = IContentProviderHolderApi.getProvider(holder);
        return provider;
    }

}
