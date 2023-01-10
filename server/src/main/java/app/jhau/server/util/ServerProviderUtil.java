package app.jhau.server.util;

import android.annotation.SuppressLint;
import android.app.ActivityManagerApi;
import android.content.AttributionSource;
import android.content.IContentProvider;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

import app.jhau.appopsmanager.IServerObserver;
import app.jhau.server.provider.ServerProvider;

public class ServerProviderUtil {

    @SuppressLint("DeprecatedSinceApi")
    public static IServerObserver getServerObserver(int userId) throws Throwable {
        IContentProvider provider;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            provider = ActivityManagerApi.getContentProviderExternalNew(ServerProvider.AUTHORITY_NAME, userId, null).provider;
        } else {
            provider = ActivityManagerApi.getContentProviderExternalNew(ServerProvider.AUTHORITY_NAME, userId, null, null).provider;
        }
        Bundle ret;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            ret = provider.call(null, ServerProvider.Method.GET_BINDER.str, null, null);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            ret = provider.call((String) null, ServerProvider.AUTHORITY_NAME, ServerProvider.Method.GET_BINDER.str, null, null);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            ret = provider.call(null, null, ServerProvider.AUTHORITY_NAME, ServerProvider.Method.GET_BINDER.str, null, null);
        } else {
            AttributionSource attributionSource = new AttributionSource.Builder(Binder.getCallingUid()).build();
            ret = provider.call(attributionSource, ServerProvider.AUTHORITY_NAME, ServerProvider.Method.GET_BINDER.str, null, null);
        }
        return IServerObserver.Stub.asInterface(ret.getBinder(ServerProvider.SERVER_OBSERVER_BINDER_KEY));
    }

}
