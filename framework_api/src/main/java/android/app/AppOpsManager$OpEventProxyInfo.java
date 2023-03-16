package android.app;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


/**
 * Proxy information for a event
 *
 * @hide
 */
@RequiresApi(Build.VERSION_CODES.R)
public class AppOpsManager$OpEventProxyInfo {
    /**
     * UID of the proxy app that noted the op
     */
    private int mUid;
    /**
     * Package of the proxy that noted the op
     */
    private @Nullable String mPackageName;
    /**
     * Attribution tag of the proxy that noted the op
     */
    private @Nullable String mAttributionTag;


    /**
     * UID of the proxy app that noted the op
     */
    public int getUid() {
        return mUid;
    }

    /**
     * Package of the proxy that noted the op
     */
    public @Nullable String getPackageName() {
        return mPackageName;
    }

    /**
     * Attribution tag of the proxy that noted the op
     */
    public @Nullable String getAttributionTag() {
        return mAttributionTag;
    }

}