package app.jhau.server.util;

import android.os.Build;

import app.jhau.server.BuildConfig;

public class Constants {
    public static final String APPLICATION_ID = BuildConfig.APPLICATION_ID;
    public static final String DEBUG_TAG = APPLICATION_ID;
    public static final String CPU_ABI = Build.SUPPORTED_ABIS[0];
    public static final String SERVER_CLASSNAME = BuildConfig.SERVER_ID + ".AppOpsServer";
    public static final String STARTER_CLASSNAME = BuildConfig.SERVER_ID + ".ServerStarter";
    public static final String SERVER_NICK_NAME = "appops_server";
    public static final String STARTER_SH_FILE_NAME = "start.sh";
}
