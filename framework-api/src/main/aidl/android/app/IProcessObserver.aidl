package android.app;

oneway interface IProcessObserver {
    //M,N,O,P,Q,R,S,T
    void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities);
    //M,N
    void onProcessStateChanged(int pid, int uid, int procState);
    //M,N,O,P,Q,R,S,T
    void onProcessDied(int pid, int uid);

    //Q,R,S,T
    void onForegroundServicesChanged(int pid, int uid, int serviceTypes);

}
