package android.app;

import androidx.annotation.NonNull;

import android.os.Build;
import android.util.LongSparseArray;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@RequiresApi(Build.VERSION_CODES.R)
public class AppOpsManager$AttributedOpEntry {

    /**
     * The code of the op
     */
    private int mOp;
    /**
     * Whether the op is running
     */
    private boolean mRunning;
    /**
     * The access events
     */
    private @Nullable LongSparseArray<AppOpsManager$NoteOpEvent> mAccessEvents;
    /**
     * The rejection events
     */
    private @Nullable LongSparseArray<AppOpsManager$NoteOpEvent> mRejectEvents;

    /**
     * Return the last access time.
     *
     * @param flags The op flags
     * @return the last access time (in milliseconds since epoch start (January 1, 1970
     * 00:00:00.000 GMT - Gregorian)) or {@code -1} if there was no access
     * @see #getLastAccessForegroundTime(int)
     * @see #getLastAccessBackgroundTime(int)
     * @see #getLastAccessTime(int, int, int)
     * @see AppOpsManager$OpEntry#getLastAccessTime(int)
     */
    public long getLastAccessTime(int flags) {
        return 0;
    }

    /**
     * Return the last foreground access time.
     *
     * @param flags The op flags
     * @return the last access time (in milliseconds since epoch start (January 1, 1970
     * 00:00:00.000 GMT - Gregorian)) or {@code -1} if there was no foreground access
     * @see #getLastAccessTime(int)
     * @see #getLastAccessBackgroundTime(int)
     * @see #getLastAccessTime(int, int, int)
     * @see AppOpsManager$OpEntry#getLastAccessForegroundTime(int)
     */
    public long getLastAccessForegroundTime(int flags) {
        return 0;
    }

    /**
     * Return the last background access time.
     *
     * @param flags The op flags
     * @return the last access time (in milliseconds since epoch start (January 1, 1970
     * 00:00:00.000 GMT - Gregorian)) or {@code -1} if there was no background access
     * @see #getLastAccessTime(int)
     * @see #getLastAccessForegroundTime(int)
     * @see #getLastAccessTime(int, int, int)
     * @see AppOpsManager$OpEntry#getLastAccessBackgroundTime(int)
     */
    public long getLastAccessBackgroundTime(int flags) {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @NonNull
    String getOpName() {
        return "";
    }

    @RequiresApi(Build.VERSION_CODES.S)
    int getOp() {
        return 0;
    }

    /**
     * Return the last access event.
     *
     * @param flags The op flags
     * @return the last access event of {@code null} if there was no access
     */
    private @Nullable AppOpsManager$NoteOpEvent getLastAccessEvent(int fromUidState, int toUidState, int flags) {
        return null;
    }

    /**
     * Return the last access time.
     *
     * @param fromUidState The lowest UID state for which to query
     * @param toUidState   The highest UID state for which to query (inclusive)
     * @param flags        The op flags
     * @return the last access time (in milliseconds since epoch start (January 1, 1970
     * 00:00:00.000 GMT - Gregorian)) or {@code -1} if there was no access
     * @see #getLastAccessTime(int)
     * @see #getLastAccessForegroundTime(int)
     * @see #getLastAccessBackgroundTime(int)
     * @see AppOpsManager$OpEntry#getLastAccessTime(int, int, int)
     */
    public long getLastAccessTime(int fromUidState, int toUidState, int flags) {
        return 0;
    }

    /**
     * Return the last rejection time.
     *
     * @param flags The op flags
     * @return the last rejection time (in milliseconds since epoch start (January 1, 1970
     * 00:00:00.000 GMT - Gregorian)) or {@code -1} if there was no rejection
     * @see #getLastRejectForegroundTime(int)
     * @see #getLastRejectBackgroundTime(int)
     * @see #getLastRejectTime(int, int, int)
     * @see AppOpsManager$OpEntry#getLastRejectTime(int)
     */
    public long getLastRejectTime(int flags) {
        return 0;
    }

    /**
     * Return the last foreground rejection time.
     *
     * @param flags The op flags
     * @return the last rejection time (in milliseconds since epoch start (January 1, 1970
     * 00:00:00.000 GMT - Gregorian)) or {@code -1} if there was no foreground rejection
     * @see #getLastRejectTime(int)
     * @see #getLastRejectBackgroundTime(int)
     * @see #getLastRejectTime(int, int, int)
     * @see AppOpsManager$OpEntry#getLastRejectForegroundTime(int)
     */
    public long getLastRejectForegroundTime(int flags) {
        return 0;
    }

    /**
     * Return the last background rejection time.
     *
     * @param flags The op flags
     * @return the last rejection time (in milliseconds since epoch start (January 1, 1970
     * 00:00:00.000 GMT - Gregorian)) or {@code -1} if there was no background rejection
     * @see #getLastRejectTime(int)
     * @see #getLastRejectForegroundTime(int)
     * @see #getLastRejectTime(int, int, int)
     * @see AppOpsManager$OpEntry#getLastRejectBackgroundTime(int)
     */
    public long getLastRejectBackgroundTime(int flags) {
        return 0;
    }

    /**
     * Return the last background rejection event.
     *
     * @param flags The op flags
     * @return the last rejection event of {@code null} if there was no rejection
     * @see #getLastRejectTime(int)
     * @see #getLastRejectForegroundTime(int)
     * @see #getLastRejectBackgroundTime(int)
     * @see AppOpsManager$OpEntry#getLastRejectTime(int, int, int)
     */
    private @Nullable AppOpsManager$NoteOpEvent getLastRejectEvent(int fromUidState, int toUidState, int flags) {
        return null;
    }

    /**
     * Return the last rejection time.
     *
     * @param fromUidState The lowest UID state for which to query
     * @param toUidState   The highest UID state for which to query (inclusive)
     * @param flags        The op flags
     * @return the last access time (in milliseconds since epoch) or {@code -1} if there was no
     * rejection
     * @see #getLastRejectTime(int)
     * @see #getLastRejectForegroundTime(int)
     * @see #getLastRejectForegroundTime(int)
     * @see #getLastRejectTime(int, int, int)
     * @see AppOpsManager$OpEntry#getLastRejectTime(int, int, int)
     */
    public long getLastRejectTime(int fromUidState, int toUidState, int flags) {
        return 0;
    }

    /**
     * Return the duration in milliseconds of the last the access.
     *
     * @param flags The op flags
     * @return the duration in milliseconds or {@code -1} if there was no rejection
     * @see #getLastForegroundDuration(int)
     * @see #getLastBackgroundDuration(int)
     * @see #getLastDuration(int, int, int)
     * @see AppOpsManager$OpEntry#getLastDuration(int)
     */
    public long getLastDuration(int flags) {
        return 0;
    }

    /**
     * Return the duration in milliseconds of the last foreground access.
     *
     * @param flags The op flags
     * @return the duration in milliseconds or {@code -1} if there was no foreground rejection
     * @see #getLastDuration(int)
     * @see #getLastBackgroundDuration(int)
     * @see #getLastDuration(int, int, int)
     * @see AppOpsManager$OpEntry#getLastForegroundDuration(int)
     */
    public long getLastForegroundDuration(int flags) {
        return 0;
    }

    /**
     * Return the duration in milliseconds of the last background access.
     *
     * @param flags The op flags
     * @return the duration in milliseconds or {@code -1} if there was no background rejection
     * @see #getLastDuration(int)
     * @see #getLastForegroundDuration(int)
     * @see #getLastDuration(int, int, int)
     * @see AppOpsManager$OpEntry#getLastBackgroundDuration(int)
     */
    public long getLastBackgroundDuration(int flags) {
        return 0;
    }

    /**
     * Return the duration in milliseconds of the last access.
     *
     * @param fromUidState The lowest UID state for which to query
     * @param toUidState   The highest UID state for which to query (inclusive)
     * @param flags        The op flags
     * @return the duration in milliseconds or {@code -1} if there was no rejection
     * @see #getLastDuration(int)
     * @see #getLastForegroundDuration(int)
     * @see #getLastBackgroundDuration(int)
     * @see #getLastDuration(int, int, int)
     * @see AppOpsManager$OpEntry#getLastDuration(int, int, int)
     */
    public long getLastDuration(int fromUidState, int toUidState, int flags) {
        return 0;
    }

    /**
     * Gets the proxy info of the app that performed the last access on behalf of this
     * attribution and as a result blamed the op on this attribution.
     *
     * @param flags The op flags
     * @return The proxy info or {@code null} if there was no proxy access
     * @see #getLastForegroundProxyInfo(int)
     * @see #getLastBackgroundProxyInfo(int)
     * @see #getLastProxyInfo(int, int, int)
     * @see AppOpsManager$OpEntry#getLastProxyInfo(int)
     */
    public @Nullable AppOpsManager$OpEventProxyInfo getLastProxyInfo(int flags) {
        return null;
    }

    /**
     * Gets the proxy info of the app that performed the last foreground access on behalf of
     * this attribution and as a result blamed the op on this attribution.
     *
     * @param flags The op flags
     * @return The proxy info or {@code null} if there was no proxy access
     * @see #getLastProxyInfo(int)
     * @see #getLastBackgroundProxyInfo(int)
     * @see #getLastProxyInfo(int, int, int)
     * @see AppOpsManager$OpEntry#getLastForegroundProxyInfo(int)
     */
    public @Nullable AppOpsManager$OpEventProxyInfo getLastForegroundProxyInfo(int flags) {
        return null;
    }

    /**
     * Gets the proxy info of the app that performed the last background access on behalf of
     * this attribution and as a result blamed the op on this attribution.
     *
     * @param flags The op flags
     * @return The proxy info or {@code null} if there was no proxy background access
     * @see #getLastProxyInfo(int)
     * @see #getLastForegroundProxyInfo(int)
     * @see #getLastProxyInfo(int, int, int)
     * @see AppOpsManager$OpEntry#getLastBackgroundProxyInfo(int)
     */
    public @Nullable AppOpsManager$OpEventProxyInfo getLastBackgroundProxyInfo(int flags) {
        return null;
    }

    /**
     * Gets the proxy info of the app that performed the last access on behalf of this
     * attribution and as a result blamed the op on this attribution.
     *
     * @param fromUidState The lowest UID state for which to query
     * @param toUidState   The highest UID state for which to query (inclusive)
     * @param flags        The op flags
     * @return The proxy info or {@code null} if there was no proxy foreground access
     * @see #getLastProxyInfo(int)
     * @see #getLastForegroundProxyInfo(int)
     * @see #getLastBackgroundProxyInfo(int)
     * @see AppOpsManager$OpEntry#getLastProxyInfo(int, int, int)
     */
    public @Nullable AppOpsManager$OpEventProxyInfo getLastProxyInfo(int fromUidState, int toUidState, int flags) {
        return null;
    }

    /**
     * Whether the op is running
     */
    public boolean isRunning() {
        return false;
    }

}