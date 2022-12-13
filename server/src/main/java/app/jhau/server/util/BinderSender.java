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
    public static void sendBinder(IBinder binder) throws Throwable {
        final IBinder token = null;
        final int userId = 0;
        final String tag = null;
        Object holder;

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            holder = ActivityManagerApi.getContentProviderExternal(ServerProvider.AUTHORITY_NAME, userId, token);
        } else {
            holder = ActivityManagerApi.getContentProviderExternal(ServerProvider.AUTHORITY_NAME, userId, token, tag);
        }

        Bundle bundle = new Bundle();
        bundle.putBinder(Constants.SERVER_BINDER_KEY, binder);
        ContentProviderHolderWrapper holderWrapper = new ContentProviderHolderWrapper(holder);
        IContentProvider provider = holderWrapper.getProvider();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            provider.call("", ServerProvider.SAVE_BINDER, "", bundle);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            provider.call("", ServerProvider.AUTHORITY_NAME, ServerProvider.SAVE_BINDER, "", bundle);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            provider.call("", "", ServerProvider.AUTHORITY_NAME, ServerProvider.SAVE_BINDER, "", bundle);
        } else {
            AttributionSource attributionSource = new AttributionSource.Builder(Binder.getCallingUid()).build();
            provider.call(attributionSource, ServerProvider.AUTHORITY_NAME, ServerProvider.SAVE_BINDER, "", bundle);
        }
    }

}
