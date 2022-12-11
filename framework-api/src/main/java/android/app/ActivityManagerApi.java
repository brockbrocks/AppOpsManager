package android.app;

import android.content.pm.IPackageManager;

import java.lang.reflect.Method;

public class ActivityManagerApi {

    /** @hide Not a real process state. */
    public static final int PROCESS_STATE_UNKNOWN = -1;

    /** @hide Process is a persistent system process. */
    public static final int PROCESS_STATE_PERSISTENT = 0;

    /** @hide Process is a persistent system process and is doing UI. */
    public static final int PROCESS_STATE_PERSISTENT_UI = 1;

    /** @hide Process is hosting the current top activities.  Note that this covers
     * all activities that are visible to the user. */
    public static final int PROCESS_STATE_TOP = 2;

    /** @hide Process is hosting a foreground service with location type. */
    public static final int PROCESS_STATE_FOREGROUND_SERVICE_LOCATION = 3;

    /** @hide Process is bound to a TOP app. This is ranked below SERVICE_LOCATION so that
     * it doesn't get the capability of location access while-in-use. */
    public static final int PROCESS_STATE_BOUND_TOP = 4;

    /** @hide Process is hosting a foreground service. */
    public static final int PROCESS_STATE_FOREGROUND_SERVICE = 5;

    /** @hide Process is hosting a foreground service due to a system binding. */
    public static final int PROCESS_STATE_BOUND_FOREGROUND_SERVICE = 6;

    /** @hide Process is important to the user, and something they are aware of. */
    public static final int PROCESS_STATE_IMPORTANT_FOREGROUND = 7;

    /** @hide Process is important to the user, but not something they are aware of. */
    public static final int PROCESS_STATE_IMPORTANT_BACKGROUND = 8;

    /** @hide Process is in the background transient so we will try to keep running. */
    public static final int PROCESS_STATE_TRANSIENT_BACKGROUND = 9;

    /** @hide Process is in the background running a backup/restore operation. */
    public static final int PROCESS_STATE_BACKUP = 10;

    /** @hide Process is in the background running a service.  Unlike oom_adj, this level
     * is used for both the normal running in background state and the executing
     * operations state. */
    public static final int PROCESS_STATE_SERVICE = 11;

    /** @hide Process is in the background running a receiver.   Note that from the
     * perspective of oom_adj, receivers run at a higher foreground level, but for our
     * prioritization here that is not necessary and putting them below services means
     * many fewer changes in some process states as they receive broadcasts. */
    public static final int PROCESS_STATE_RECEIVER = 12;

    /** @hide Same as {@link #PROCESS_STATE_TOP} but while device is sleeping. */
    public static final int PROCESS_STATE_TOP_SLEEPING = 13;

    /** @hide Process is in the background, but it can't restore its state so we want
     * to try to avoid killing it. */
    public static final int PROCESS_STATE_HEAVY_WEIGHT = 14;

    /** @hide Process is in the background but hosts the home activity. */
    public static final int PROCESS_STATE_HOME = 15;

    /** @hide Process is in the background but hosts the last shown activity. */
    public static final int PROCESS_STATE_LAST_ACTIVITY = 16;

    /** @hide Process is being cached for later use and contains activities. */
    public static final int PROCESS_STATE_CACHED_ACTIVITY = 17;

    /** @hide Process is being cached for later use and is a client of another cached
     * process that contains activities. */
    public static final int PROCESS_STATE_CACHED_ACTIVITY_CLIENT = 18;

    /** @hide Process is being cached for later use and has an activity that corresponds
     * to an existing recent task. */
    public static final int PROCESS_STATE_CACHED_RECENT = 19;

    /** @hide Process is being cached for later use and is empty. */
    public static final int PROCESS_STATE_CACHED_EMPTY = 20;

    /** @hide Process does not exist. */
    public static final int PROCESS_STATE_NONEXISTENT = 21;



    /** @hide Flag for registerUidObserver: report changes in process state. */
    public static final int UID_OBSERVER_PROCSTATE = 1<<0;

    /** @hide Flag for registerUidObserver: report uid gone. */
    public static final int UID_OBSERVER_GONE = 1<<1;

    /** @hide Flag for registerUidObserver: report uid has become idle. */
    public static final int UID_OBSERVER_IDLE = 1<<2;

    /** @hide Flag for registerUidObserver: report uid has become active. */
    public static final int UID_OBSERVER_ACTIVE = 1<<3;

    /** @hide Flag for registerUidObserver: report uid cached state has changed. */
    public static final int UID_OBSERVER_CACHED = 1<<4;

    public static IActivityManager getService() {
        try {
            Method method = ActivityManager.class.getDeclaredMethod("getService");
            method.setAccessible(true);
            return (IActivityManager) method.invoke(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IPackageManager getPackageManager() {
        try {
            Method getPackageManagerMethod = ActivityThread.class.getDeclaredMethod("getPackageManager");
            getPackageManagerMethod.setAccessible(true);
            return (IPackageManager) getPackageManagerMethod.invoke(null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

}
