package app.jhau.server;

import android.os.FileObserver;

import java.io.File;

public class ApkObserverCompat extends FileObserver {
    private Callback callback;

    private ApkObserverCompat(String path, int mask) {
        super(path, mask);
    }

    private ApkObserverCompat(File file, int mask) {
        super(file, mask);
    }

    @Override
    public void onEvent(int event, String path) {
        if (event == FileObserver.DELETE) {
            stopWatching();
            if (callback != null) callback.call();
        }
    }

    public interface Callback {
        void call();
    }

    public static class Build {

        private String filePath;
        private Callback callback;

        public Build filePath(String path) {
            filePath = path;
            return this;
        }

        public Build setCallback(Callback callback) {
            this.callback = callback;
            return this;
        }

        public ApkObserverCompat build() {
            ApkObserverCompat apkObserver;
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
                apkObserver = new ApkObserverCompat(filePath, FileObserver.DELETE);
            } else {
                apkObserver = new ApkObserverCompat(new File(filePath), FileObserver.DELETE);
            }
            apkObserver.callback = callback;
            filePath = null;
            callback = null;
            return apkObserver;
        }
    }
}
