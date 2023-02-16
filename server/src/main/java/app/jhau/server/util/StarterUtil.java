package app.jhau.server.util;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import app.jhau.server.BuildConfig;

public class StarterUtil {

    private static final String TAG = Constants.DEBUG_TAG;

    public static String getCommand(Context context) {
        String packagePath = context.getPackageResourcePath();
        return "adb shell app_process -Djava.class.path=" + packagePath + " /system/bin " + Constants.STARTER_CLASSNAME;
    }

    public static void createShellFile(Context ctx) throws Throwable {
        final String filePath = ctx.getExternalCacheDir() + "/" + Constants.STARTER_SH_FILE_NAME;
        File file = new File(filePath);
        if (file.exists() && !BuildConfig.DEBUG) return;
        BufferedReader br = new BufferedReader(new InputStreamReader(ctx.getAssets().open(Constants.STARTER_SH_FILE_NAME)));
        String tmp;
        StringBuilder content = new StringBuilder();
        while ((tmp = br.readLine()) != null) {
            tmp = tmp.replace("%%%package%%%", Constants.APPLICATION_ID);
            tmp = tmp.replace("%%%classpath%%%", ctx.getPackageResourcePath());
            tmp = tmp.replace("%%%sdk%%%", String.valueOf(Build.VERSION.SDK_INT));
            tmp = tmp.replace("%%%libpath%%%", ctx.getPackageResourcePath() + "!/lib/" + Build.SUPPORTED_ABIS[0]);
            tmp = tmp.replace("%%%starterclassname%%%", Constants.STARTER_CLASSNAME);
            content.append(tmp);
            content.append("\n");
        }
        FileWriter fw = new FileWriter(file);
        fw.write(content.toString());
        fw.close();
        br.close();
    }

    public static boolean checkServerExist(Application application) {
        try {
            Method method = application.getClass().getMethod("getIServer");
            return method.invoke(application) != null;
        } catch (Throwable e) {
            return false;
        }
    }
}
