package app.demo.framework;

import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import app.demo.framework.service.IAdd;

public class BinderCursor extends MatrixCursor {

    private final Bundle mExtras;

    public BinderCursor() {
        super(new String[]{""});
        mExtras = new Bundle();
        mExtras.putBinder("binder", new IAdd.Stub() {
            @Override
            public int add(int a, int b) throws RemoteException {
                return a+b;
            }
        });
    }

    @Override
    public Bundle getExtras() {
        return mExtras;
    }
}
