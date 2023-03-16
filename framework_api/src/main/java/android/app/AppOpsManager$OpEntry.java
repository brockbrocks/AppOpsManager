package android.app;

import android.os.Build;

import androidx.annotation.DeprecatedSinceApi;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Collections;
import java.util.Map;

public class AppOpsManager$OpEntry {
    public int getOp() {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public @NonNull String getOpStr() {
        return null;
    }

    /**
     * @deprecated Use {@link #getLastAccessTime(int)} instead
     */
    @DeprecatedSinceApi(api = Build.VERSION_CODES.R)
    public long getTime() {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @DeprecatedSinceApi(api = Build.VERSION_CODES.Q)
    public long getLastAccessTime() {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public long getLastAccessTime(int flags) {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @DeprecatedSinceApi(api = Build.VERSION_CODES.Q)
    public long getLastAccessForegroundTime() {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public long getLastAccessForegroundTime(int flags) {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @DeprecatedSinceApi(api = Build.VERSION_CODES.Q)
    public long getLastAccessBackgroundTime() {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public long getLastAccessBackgroundTime(int flags) {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public long getLastAccessTime(int fromUidState, int toUidState, int flags) {
        return 0;
    }

    /**
     * @deprecated
     */
    @RequiresApi(Build.VERSION_CODES.P)
    @DeprecatedSinceApi(api = Build.VERSION_CODES.Q)
    public long getLastTimeFor(int uidState) {
        return 0;
    }

    /**
     * @deprecated Use {@link #getLastRejectTime(int)} instead
     */
    @DeprecatedSinceApi(api = Build.VERSION_CODES.R)
    public long getRejectTime() {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public long getLastRejectTime(int flags) {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public long getLastRejectForegroundTime(int flags) {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public long getLastRejectBackgroundTime(int flags) {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public long getLastRejectTime(int fromUidState, int toUidState, int flags) {
        return 0;
    }

    public boolean isRunning() {
        return false;
    }

//    @DeprecatedSinceApi(api = Build.VERSION_CODES.Q)
//    public int getDuration() {
//        return 0;
//    }

    /**
     * @deprecated Use {@link #getLastDuration(int)} instead
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    @DeprecatedSinceApi(api = Build.VERSION_CODES.R)
    public long getDuration() {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.R)
    public long getLastDuration(int flags) {
        return 0;
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    public long getLastForegroundDuration(int flags) {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public long getLastBackgroundDuration(int flags) {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    public long getLastDuration(int fromUidState, int toUidState, int flags) {
        return 0;
    }

    /**
     * @deprecated Use {@link #getLastProxyInfo(int)} instead
     */
    @DeprecatedSinceApi(api = Build.VERSION_CODES.R)
    public int getProxyUid() {
        return 0;
    }

    /**
     * @deprecated Use {@link #getLastProxyInfo(int)} instead
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    @DeprecatedSinceApi(api = Build.VERSION_CODES.R)
    public int getProxyUid(int uidState, int flags) {
        return 0;
    }

    /**
     * @deprecated Use {@link #getLastProxyInfo(int)} instead
     */
    @DeprecatedSinceApi(api = Build.VERSION_CODES.R)
    public String getProxyPackageName() {
        return null;
    }

    /**
     * @deprecated Use {@link #getLastProxyInfo(int)} instead
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    @DeprecatedSinceApi(api = Build.VERSION_CODES.R)
    public @Nullable String getProxyPackageName(int uidState, int flags) {
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.R)
    public @Nullable AppOpsManager$OpEventProxyInfo getLastProxyInfo(int flags) {
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.R)
    public @Nullable AppOpsManager$OpEventProxyInfo getLastForegroundProxyInfo(int flags) {
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.R)
    public @Nullable AppOpsManager$OpEventProxyInfo getLastBackgroundProxyInfo(int flags) {
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.R)
    public @Nullable AppOpsManager$OpEventProxyInfo getLastProxyInfo(int fromUidState, int toUidState, int flags) {
        return null;
    }

    public int getMode() {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.R)
    public @NonNull Map<String, AppOpsManager$AttributedOpEntry> getAttributedOpEntries() {
        return Collections.emptyMap();
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private @Nullable AppOpsManager$NoteOpEvent getLastAccessEvent(int fromUidState, int toUidState, int flags) {
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private @Nullable AppOpsManager$NoteOpEvent getLastRejectEvent(int fromUidState, int toUidState, int flags) {
        return null;
    }
}
