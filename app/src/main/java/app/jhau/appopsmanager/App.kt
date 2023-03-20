package app.jhau.appopsmanager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import app.jhau.server.IServer
import app.jhau.server.IServerActivatedObserver
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.atomic.AtomicInteger

@HiltAndroidApp
class App : Application() {
    private val TAG = "App"

    var iServer: IServer? = null

    var iServerActivatedObserver: IServerActivatedObserver? = null

    private val activityCount = AtomicInteger(0)
//    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
//        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
//            activityCount.addAndGet(1)
//        }
//
//        override fun onActivityStarted(activity: Activity) {
//
//        }
//
//        override fun onActivityResumed(activity: Activity) {
//
//        }
//
//        override fun onActivityPaused(activity: Activity) {
//
//        }
//
//        override fun onActivityStopped(activity: Activity) {
//
//        }
//
//        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
//
//        }
//
//        override fun onActivityDestroyed(activity: Activity) {
//            if (activityCount.decrementAndGet() == 0) {
//                Log.i(TAG, "onActivityDestroyed: $activity")
//            }
//        }
//    }

    override fun onCreate() {
        super.onCreate()
//        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        Log.i(TAG,"onCreate: App. uid=${android.os.Process.myUid()}, pid=${android.os.Process.myPid()}")
    }

    fun onSetIServerToApplication(binder: IBinder) {
        iServer = IServer.Stub.asInterface(binder)
        if (iServerActivatedObserver != null) {
            Log.i(TAG, "registerServerActivatedObserverOnce")
            iServer?.registerServerActivatedObserverOnce(iServerActivatedObserver)
        }
    }

    fun onServerKill() {
        iServer = null
        iServerActivatedObserver = null
    }

//    companion object {
//        lateinit var application: Application
//    }
}