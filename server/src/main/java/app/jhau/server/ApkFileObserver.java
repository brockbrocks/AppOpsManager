package app.jhau.server;

import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;

public class ApkFileObserver extends FileObserver {
    private final String TAG = this.getClass().getSimpleName();
    private Callback callback;

    private ApkFileObserver(String path, int mask) {
        super(path, mask);
    }

    @RequiresApi(api = android.os.Build.VERSION_CODES.Q)
    private ApkFileObserver(File file, int mask) {
        super(file, mask);
    }

    @Override
    public void onEvent(int event, String path) {
        switch (event & ALL_EVENTS) {
//            case ACCESS:
//                Log.i(TAG, "onEvent: ACCESS, path=" + path);
//                break;
//            case MODIFY:
//                Log.i(TAG, "onEvent: MODIFY, path=" + path);
//                break;
//            case ATTRIB:
//                Log.i(TAG, "onEvent: ATTRIB, path=" + path);
//                break;
//            case CLOSE_WRITE:
//                Log.i(TAG, "onEvent: CLOSE_WRITE, path=" + path);
//                break;
//            case CLOSE_NOWRITE:
//                Log.i(TAG, "onEvent: CLOSE_NOWRITE, path=" + path);
//                break;
//            case OPEN:
//                Log.i(TAG, "onEvent: OPEN, path=" + path);
//                break;
//            case MOVED_FROM:
//                Log.i(TAG, "onEvent: MOVED_FROM, path=" + path);
//                break;
//            case MOVED_TO:
//                Log.i(TAG, "onEvent: MOVED_TO, path=" + path);
//                break;
//            case CREATE:
//                Log.i(TAG, "onEvent: CREATE, path=" + path);
//                break;
            case DELETE:
                Log.i(TAG, "onEvent: DELETE, path=" + path);
                if (callback != null) callback.call();
                break;
//            case DELETE_SELF:
//                Log.i(TAG, "onEvent: DELETE_SELF, path=" + path);
//                break;
//            case MOVE_SELF:
//                Log.i(TAG, "onEvent: MOVE_SELF, path=" + path);
//                break;
            default:
                Log.i(TAG, "onEvent: unknown=" + event + ", path=" + path);
                break;
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

        public ApkFileObserver build() {
            ApkFileObserver apkObserver;
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
                apkObserver = new ApkFileObserver(filePath, DELETE);
            } else {
                apkObserver = new ApkFileObserver(new File(filePath), DELETE);
            }
            apkObserver.callback = callback;
            return apkObserver;
        }
    }
}
