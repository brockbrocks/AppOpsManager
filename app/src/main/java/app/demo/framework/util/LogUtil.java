package app.demo.framework.util;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.FileOutputStream;

public class LogUtil {
    @SuppressLint("SdCardPath")
    private static final String defaultPath = "/sdcard/Android/data/app.demo.framework/cache";
//    private static final String defaultPath = "/data/local/tmp";
    private static final String defaultFileName = "app.log";

    public static void writeToFile(String log, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(log.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeToFile(String log) {
        try {
            File file = new File(defaultPath, defaultFileName);
            if (file.exists()) {
                file.createNewFile();
            }
            writeToFile(log, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
