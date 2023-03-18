package app.jhau.framework.permission;

import android.content.pm.PermissionInfo;

import java.lang.reflect.Field;

import app.jhau.framework.util.ReflectUtil;

public class PermissionInfoHidden {

    //hidden protection flag
    public static int PROTECTION_FLAG_OEM;
    public static int PROTECTION_FLAG_VENDOR_PRIVILEGED;
    public static int PROTECTION_FLAG_SYSTEM_TEXT_CLASSIFIER;
    public static int PROTECTION_FLAG_CONFIGURATOR;
    public static int PROTECTION_FLAG_INCIDENT_REPORT_APPROVER;
    public static int PROTECTION_FLAG_APP_PREDICTOR;
    public static int PROTECTION_FLAG_COMPANION;
    public static int PROTECTION_FLAG_RETAIL_DEMO;
    public static int PROTECTION_FLAG_RECENTS;
    public static int PROTECTION_FLAG_ROLE;
    public static int PROTECTION_FLAG_KNOWN_SIGNER;

    static {
        for (Field field : PermissionInfo.class.getFields()) {
            if (field.getName().equals("PROTECTION_FLAG_OEM")) {
                PROTECTION_FLAG_OEM = ReflectUtil.getStaticField(field);
            }
            if (field.getName().equals("PROTECTION_FLAG_VENDOR_PRIVILEGED")) {
                PROTECTION_FLAG_VENDOR_PRIVILEGED = ReflectUtil.getStaticField(field);
            }
            if (field.getName().equals("PROTECTION_FLAG_SYSTEM_TEXT_CLASSIFIER")) {
                PROTECTION_FLAG_SYSTEM_TEXT_CLASSIFIER = ReflectUtil.getStaticField(field);
            }
            if (field.getName().equals("PROTECTION_FLAG_CONFIGURATOR")) {
                PROTECTION_FLAG_CONFIGURATOR = ReflectUtil.getStaticField(field);
            }
            if (field.getName().equals("PROTECTION_FLAG_INCIDENT_REPORT_APPROVER")) {
                PROTECTION_FLAG_INCIDENT_REPORT_APPROVER = ReflectUtil.getStaticField(field);
            }
            if (field.getName().equals("PROTECTION_FLAG_APP_PREDICTOR")) {
                PROTECTION_FLAG_APP_PREDICTOR = ReflectUtil.getStaticField(field);
            }
            if (field.getName().equals("PROTECTION_FLAG_COMPANION")) {
                PROTECTION_FLAG_COMPANION = ReflectUtil.getStaticField(field);
            }
            if (field.getName().equals("PROTECTION_FLAG_RETAIL_DEMO")) {
                PROTECTION_FLAG_RETAIL_DEMO = ReflectUtil.getStaticField(field);
            }
            if (field.getName().equals("PROTECTION_FLAG_RECENTS")) {
                PROTECTION_FLAG_RECENTS = ReflectUtil.getStaticField(field);
            }
            if (field.getName().equals("PROTECTION_FLAG_ROLE")) {
                PROTECTION_FLAG_ROLE = ReflectUtil.getStaticField(field);
            }
            if (field.getName().equals("PROTECTION_FLAG_KNOWN_SIGNER")) {
                PROTECTION_FLAG_KNOWN_SIGNER = ReflectUtil.getStaticField(field);
            }
        }
    }
}
