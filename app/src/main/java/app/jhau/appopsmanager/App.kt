package app.jhau.appopsmanager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import app.jhau.server.IAppOpsServer
import app.jhau.server.IServerActivatedObserver
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.atomic.AtomicInteger

@HiltAndroidApp
class App : Application() {
    private val TAG = "App"

    lateinit var iAppOpsServer: IAppOpsServer

    lateinit var iServerActivatedObserver: IServerActivatedObserver

    private val activityCount = AtomicInteger(0)
    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            activityCount.addAndGet(1)
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {
//            if (activityCount.get() == 1) {
//                _iAppOpsServer = null
//                _iServerActivatedObserver = null
//                Log.i(TAG, "onActivityStopped: $activity")
//            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activityCount.decrementAndGet() == 0) {
                Log.i(TAG, "onActivityDestroyed: $activity")
                android.os.Process.killProcess(android.os.Process.myPid())
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    fun onSetIAppOpsServerToApplication(binder: IBinder) {
        this.iAppOpsServer = IAppOpsServer.Stub.asInterface(binder)
        if (::iServerActivatedObserver.isInitialized) {
            Log.i(TAG, "registerServerActivatedObserverOnce")
            this.iAppOpsServer.registerServerActivatedObserverOnce(iServerActivatedObserver)
        }
    }
}