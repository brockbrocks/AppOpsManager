package app.jhau.server.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import app.jhau.server.BinderCursor;
import app.jhau.server.BuildConfig;
import app.jhau.server.util.Constants;

public class ServerProvider extends ContentProvider {
    private static final String TAG = Constants.DEBUG_TAG;

    public static final String SERVER_BINDER_KEY = "binder";
    public static final String AUTHORITY_NAME = BuildConfig.SERVER_ID + ".provider";
    public static final String AUTHORITY_URI = "content://" + AUTHORITY_NAME;

    public static final String SAVE_BINDER = "save_binder";
    public static final String GET_BINDER = "get_binder";

    private IBinder serverBinder;

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        BinderCursor cursor = new BinderCursor();
        cursor.getExtras().putBinder(SERVER_BINDER_KEY, serverBinder);
        return cursor;
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
        Bundle bundle = null;
        switch (method) {
            case SAVE_BINDER:
                this.serverBinder = extras.getBinder(SERVER_BINDER_KEY);
                Log.i(TAG, "call: " + SAVE_BINDER);
                break;
            case GET_BINDER:
                bundle = new Bundle();
                bundle.putBinder(SERVER_BINDER_KEY, serverBinder);
                Log.i(TAG, "call: " + GET_BINDER);
                break;
            default:
                break;
        }
        return bundle;
    }

}