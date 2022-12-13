package app.jhau.server.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Build;
import android.os.IBinder;
import android.util.ArrayMap;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ContentValuesUtil {
    @SuppressLint("BlockedPrivateApi")
    public static ContentValues putBinder(ContentValues values, IBinder binder) throws Throwable {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Field mValuesField = values.getClass().getDeclaredField("mValues");
            mValuesField.setAccessible(true);
            HashMap<String, Object> mValues = (HashMap<String, Object>) mValuesField.get(values);
            mValues.put(Constants.SERVER_BINDER_KEY, binder);
            return values;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Field mValuesField = values.getClass().getDeclaredField("mMap");
            mValuesField.setAccessible(true);
            ArrayMap<String, Object> mValues = (ArrayMap<String, Object>) mValuesField.get(values);
            mValues.put(Constants.SERVER_BINDER_KEY, binder);
            return values;
        }

        throw new IllegalStateException();
    }
}
