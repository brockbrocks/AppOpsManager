package android.content;

import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;


public interface IContentProvider extends IInterface {
    public Bundle call(String callingPkg, String authority, String method,
                       String arg, Bundle extras) throws RemoteException;

    public int update(String callingPkg, Uri url, ContentValues values, String selection,
                      String[] selectionArgs) throws RemoteException;

    public int update(String callingPkg, String attributionTag, Uri url, ContentValues values,
                      Bundle extras) throws RemoteException;
}

