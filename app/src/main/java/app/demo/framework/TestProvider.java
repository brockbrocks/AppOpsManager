package app.demo.framework;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class TestProvider extends ContentProvider {

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return -1;
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
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return new BinderCursor();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return -1;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String authority, @NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Bundle bundle = new Bundle();
        Binder binder = null;
        bundle.putBinder("binder", binder);
        return bundle;
    }
}