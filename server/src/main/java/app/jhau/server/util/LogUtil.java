package app.jhau.server.util;

import android.annotation.SuppressLint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import app.jhau.server.BuildConfig;

public class LogUtil {
    @SuppressLint("SdCardPath")
    private static final String defaultPath = "/sdcard/Android/data/" + BuildConfig.APPLICATION_ID + "/cache";
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
            if (!file.exists()) {
                file.createNewFile();
            }
            writeToFile(log, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void appendToFile(String log) {
        try {
            FileWriter fw = new FileWriter(defaultPath + "/" + defaultFileName, true);
            fw.write(log);
            fw.write("\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendToDataLocalTmpDir(String log) {
        try {
            FileWriter fw = new FileWriter("/data/local/tmp/app.log", true);
            fw.write(log);
            fw.write("\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
