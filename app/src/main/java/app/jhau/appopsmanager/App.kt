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

    lateinit var iServer: IServer

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

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity) {
            if (activityCount.decrementAndGet() == 0) {
                Log.i(TAG, "onActivityDestroyed: $activity")
                //todo: fixes bug
//                android.os.Process.killProcess(android.os.Process.myPid())
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        Log.i("tttt","onCreate: App. uid=${android.os.Process.myUid()}, pid=${android.os.Process.myPid()}")
    }

    fun onSetIServerToApplication(binder: IBinder) {
        this.iServer = IServer.Stub.asInterface(binder)
        if (::iServerActivatedObserver.isInitialized) {
            Log.i(TAG, "registerServerActivatedObserverOnce")
            this.iServer.registerServerActivatedObserverOnce(iServerActivatedObserver)
        }
    }

    companion object {
        lateinit var application: Application
    }
}