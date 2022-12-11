package app.jhau.server;

import android.database.MatrixCursor;
import android.os.Bundle;

public class BinderCursor extends MatrixCursor {

    private final Bundle mExtras;

    public BinderCursor() {
        super(new String[]{""});
        mExtras = new Bundle();
        mExtras.putBinder("binder", null);
    }

    @Override
    public Bundle getExtras() {
        return mExtras;
    }
}
