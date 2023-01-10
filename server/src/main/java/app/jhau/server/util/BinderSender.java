package app.jhau.server.util;

import android.app.ActivityManagerApi;
import android.content.AttributionSource;
import android.content.IContentProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import app.jhau.framework.ContentProviderHolderWrapper;
import app.jhau.server.provider.ServerProvider;

public class BinderSender {
    private static final String TAG = Constants.DEBUG_TAG;

    public static void sendBinder(IBinder binder) throws Throwable {
        Log.i(TAG, "sendBinder(), pid=" + android.os.Process.myPid());
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
        bundle.putBinder(ServerProvider.SERVER_BINDER_KEY, binder);

        IContentProvider provider = new ContentProviderHolderWrapper(holder).provider;
//        IContentProvider provider = new ContentProviderHolderWrapper(holder).getProvider();
//        IContentProvider provider = new ContentProviderHolderWrapper(holder).provider;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            provider.call("", ServerProvider.Method.SAVE_BINDER.key, "", bundle);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            provider.call("", ServerProvider.AUTHORITY_NAME, ServerProvider.Method.SAVE_BINDER.key, "", bundle);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            provider.call("", "", ServerProvider.AUTHORITY_NAME, ServerProvider.Method.SAVE_BINDER.key, "", bundle);
        } else {
            AttributionSource attributionSource = new AttributionSource.Builder(android.os.Process.myUid()).build();
            provider.call(attributionSource, ServerProvider.AUTHORITY_NAME, ServerProvider.Method.SAVE_BINDER.key, "", bundle);
        }
    }

}
