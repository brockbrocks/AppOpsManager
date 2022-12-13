package android.content;

import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;

public interface IContentProvider extends IInterface {

    //Android M-P
    public Bundle call(String callingPkg, String method, String arg, Bundle extras)
            throws RemoteException;

    //Android Q
    public Bundle call(String callingPkg, String authority, String method, String arg, Bundle extras)
            throws RemoteException;

    //Android R
    public Bundle call(String callingPkg, String attributionTag, String authority, String method, String arg, Bundle extras)
            throws RemoteException;

    //Android S-T
    public Bundle call(AttributionSource attributionSource, String authority,
                       String method, String arg, Bundle extras) throws RemoteException;
}

