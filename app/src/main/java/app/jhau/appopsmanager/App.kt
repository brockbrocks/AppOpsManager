package app.jhau.appopsmanager

import android.app.Application
import android.os.IBinder
import android.util.Log
import app.jhau.server.IServer
import app.jhau.server.IServerActivatedObserver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    private val TAG = "App"

    var iServer: IServer? = null

    var iServerActivatedObserver: IServerActivatedObserver? = null

    override fun onCreate() {
        super.onCreate()
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
}