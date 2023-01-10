package app.jhau.server.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.lang.ref.WeakReference;

import app.jhau.server.BuildConfig;
import app.jhau.server.util.Constants;

public class ServerProvider extends ContentProvider {
    private static final String TAG = Constants.DEBUG_TAG;

    public static final String SERVER_BINDER_KEY = "server_binder";
    public static final String SERVER_OBSERVER_BINDER_KEY = "server_observer_binder";
    public static final String AUTHORITY_NAME = BuildConfig.SERVER_ID + ".provider";
    public static final String AUTHORITY_URI = "content://" + AUTHORITY_NAME;

//    public static final String SAVE_BINDER = "save_binder";
//    public static final String GET_BINDER = "get_binder";

    private IBinder server;

    private WeakReference<IBinder> serverObserver;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        Bundle bundle = new Bundle();
        Method methodEnum = Method.parse(method);
        if (methodEnum == null) return bundle;
        //get_binder
        if (methodEnum == Method.GET_BINDER) {
            bundle.putBinder(SERVER_BINDER_KEY, server);
            IBinder serverObserver = null;
            if (this.serverObserver != null) {
                serverObserver = this.serverObserver.get();
            }
            bundle.putBinder(SERVER_OBSERVER_BINDER_KEY, serverObserver);
            Log.i(TAG, "call: " + Method.GET_BINDER + ", server=" + server + ", serverObserver=" + serverObserver);
            return bundle;
        }
        //save_binder
        if (methodEnum == Method.SAVE_BINDER) {
            this.server = extras.getBinder(SERVER_BINDER_KEY);
            Log.i(TAG, "call: " + Method.SAVE_BINDER + ", binder=" + server);
            return bundle;
        }
        //add_server_observer
        if (methodEnum == Method.ADD_SERVER_OBSERVER) {
            IBinder binder = extras.getBinder(Method.ADD_SERVER_OBSERVER.key);
            serverObserver = new WeakReference<>(binder);
            Log.i(TAG, "call: " + Method.ADD_SERVER_OBSERVER + ", binder=" + serverObserver.get());
            return bundle;
        }
        return bundle;
    }

    public enum Method {
        GET_BINDER("get_binder"),
        SAVE_BINDER("save_binder"),
        ADD_SERVER_OBSERVER("add_server_observer"),
        UNKNOWN("unknown");

        public final String str;
        public final String key;

        Method(String methodStr) {
            this.str = methodStr;
            this.key = methodStr;
        }

        @Override
        public String toString() {
            return str;
        }

        public static Method parse(String methodStr) {
            for (Method method : Method.values()) {
                if (methodStr.equals(method.str)) return method;
            }
            return Method.UNKNOWN;
        }
    }
}