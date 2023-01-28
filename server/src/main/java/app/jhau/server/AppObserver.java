package app.jhau.server;

import android.os.Build;
import android.os.FileObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.File;

import app.jhau.framework.pms.PackageManagerApi;
import app.jhau.server.util.Constants;

public class AppObserver {

    private AppFileObserver appFileObserver;
    private AppObserverEventListener eventListener;
    private int userId;

    public void observe(String path, int userId, AppObserverEventListener listener) {
        stopObserve();
        this.userId = userId;
        eventListener = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appFileObserver = new AppFileObserver(new File(path), FileObserver.ALL_EVENTS);
        } else {
            appFileObserver = new AppFileObserver(path, FileObserver.ALL_EVENTS);
        }
        appFileObserver.startWatching();
    }

    public void stopObserve() {
        if (appFileObserver != null) {
            appFileObserver.stopWatching();
        }
        eventListener = null;
        userId = -1;
    }

    enum Event {
        REMOVE,
        UPDATE
    }

    class AppFileObserver extends FileObserver {

        public AppFileObserver(String path, int mask) {
            super(path, mask);
        }

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public AppFileObserver(@NonNull File file, int mask) {
            super(file, mask);
        }

        @Override
        public void onEvent(int event, @Nullable String path) {
            switch (event & ALL_EVENTS) {
                case DELETE:
                    boolean isPackageAvailable;
                    try {
                        isPackageAvailable = PackageManagerApi.getInstance().isPackageAvailable(Constants.APPLICATION_ID, userId);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        isPackageAvailable = false;
                    }
                    if (isPackageAvailable) {
                        eventListener.onEvent(Event.UPDATE);
                    } else {
                        eventListener.onEvent(Event.REMOVE);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    interface AppObserverEventListener {
        void onEvent(Event event);
    }
}
