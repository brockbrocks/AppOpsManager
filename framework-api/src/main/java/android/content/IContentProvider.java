package android.content;

import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;

public interface IContentProvider extends IInterface {
    public Bundle call(String callingPkg, String authority, String method,
                       String arg, Bundle extras) throws RemoteException;
}
