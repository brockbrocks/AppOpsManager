package app.jhau.server.util;

import android.os.Build;

import app.jhau.server.BuildConfig;

public class Constants {
    public static final String APPLICATION_ID = BuildConfig.APPLICATION_ID;
    public static final String DEBUG_TAG = APPLICATION_ID;
    public static final String CPU_ABI = Build.SUPPORTED_ABIS[0];
}
