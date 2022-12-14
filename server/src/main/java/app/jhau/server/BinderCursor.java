package app.jhau.server;

import android.database.MatrixCursor;
import android.os.Bundle;

import app.jhau.server.provider.ServerProvider;

public class BinderCursor extends MatrixCursor {

    private final Bundle mExtras;

    public BinderCursor() {
        super(new String[]{""});
        mExtras = new Bundle();
        mExtras.putBinder(ServerProvider.SERVER_BINDER_KEY, null);
    }

    @Override
    public Bundle getExtras() {
        return mExtras;
    }
}
