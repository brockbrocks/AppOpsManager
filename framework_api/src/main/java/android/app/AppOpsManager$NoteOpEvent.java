package android.app;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@RequiresApi(Build.VERSION_CODES.R)
public class AppOpsManager$NoteOpEvent {
    /**
     * Time of noteOp event
     */
    private long mNoteTime;
    /**
     * The duration of this event (in case this is a startOp event, -1 otherwise).
     */
    private long mDuration;
    /**
     * Proxy information of the noteOp event
     */
    private @Nullable AppOpsManager$OpEventProxyInfo mProxy;


    /**
     * Time of noteOp event
     */
    public long getNoteTime() {
        return mNoteTime;
    }

    /**
     * The duration of this event (in case this is a startOp event, -1 otherwise).
     */
    public long getDuration() {
        return mDuration;
    }

    /**
     * Proxy information of the noteOp event
     */
    public @Nullable AppOpsManager$OpEventProxyInfo getProxy() {
        return mProxy;
    }
}