package android.app;

oneway interface IProcessObserver {
    void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities);
    void onForegroundServicesChanged(int pid, int uid, int serviceTypes);
    void onProcessDied(int pid, int uid);
}
