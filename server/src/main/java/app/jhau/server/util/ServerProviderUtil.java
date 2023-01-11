package app.jhau.server.util;

import android.annotation.SuppressLint;
import android.content.AttributionSource;
import android.content.IContentProvider;
import android.os.Build;
import android.os.Bundle;

import app.jhau.framework_api.ActivityManagerApi;
import app.jhau.server.AppOpsServer;
import app.jhau.server.provider.ServerProvider;

public class ServerProviderUtil {

    @SuppressLint("DeprecatedSinceApi")
    public static void sendServerBinderToApplication(AppOpsServer.AppOpsServerThread serverThread, int userId) throws Throwable{
        IContentProvider provider;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            provider = ActivityManagerApi.getContentProviderExternal(ServerProvider.AUTHORITY_NAME, userId, null).provider;
        } else {
            provider = ActivityManagerApi.getContentProviderExternal(ServerProvider.AUTHORITY_NAME, userId, null, null).provider;
        }

        Bundle bundle = new Bundle();
        bundle.putBinder(ServerProvider.SERVER_BINDER_KEY, serverThread.asBinder());
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            provider.call(null, ServerProvider.Method.SAVE_BINDER.str, null, bundle);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            provider.call((String) null, ServerProvider.AUTHORITY_NAME, ServerProvider.Method.SAVE_BINDER.str, null, bundle);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
            provider.call(null, null, ServerProvider.AUTHORITY_NAME, ServerProvider.Method.SAVE_BINDER.str, null, bundle);
        } else {
            AttributionSource attributionSource = new AttributionSource.Builder(android.os.Process.myUid()).build();
            provider.call(attributionSource, ServerProvider.AUTHORITY_NAME, ServerProvider.Method.SAVE_BINDER.str, null, bundle);
        }
    }
}
