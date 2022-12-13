package app.jhau.server.util;

import android.app.ActivityManagerApi;
import android.content.AttributionSource;
import android.content.ContentValues;
import android.content.IContentProvider;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import app.jhau.server.provider.ServerProvider;

public class BinderSender {
    public static void sendBinder(IBinder binder) {
        try {
            IContentProvider provider = getContentProvider();
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                ContentValues values = ContentValuesUtil.putBinder(new ContentValues(), binder);
                provider.update("", Uri.parse(ServerProvider.AUTHORITY_URI), values, null, null);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                Bundle bundle = new Bundle();
                bundle.putBinder(Constants.SERVER_BINDER_KEY, binder);
                Log.i("ServerProvider", "sendBinder: ");
                provider.call("", "", ServerProvider.AUTHORITY_NAME, ServerProvider.SAVE_BINDER, "", bundle);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Bundle bundle = new Bundle();
                bundle.putBinder(Constants.SERVER_BINDER_KEY, binder);
                provider.call(new AttributionSource.Builder(Binder.getCallingUid()).build(), ServerProvider.AUTHORITY_NAME, ServerProvider.SAVE_BINDER, "", bundle);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static IContentProvider getContentProvider() throws Throwable {
        final IBinder token = null;
        final int userId = 0;
        final String tag = null;
        Object holder;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            holder = ActivityManagerApi.getContentProviderExternal(ServerProvider.AUTHORITY_NAME, userId, token);
        } else {
            holder = ActivityManagerApi.getContentProviderExternal(ServerProvider.AUTHORITY_NAME, userId, token, tag);
        }
        ContentProviderHolderWrapper holderWrapper = new ContentProviderHolderWrapper(holder);
        IContentProvider provider = holderWrapper.getProvider();
        return provider;
    }

}
