-keep public class android.app.ActivityManagerNative{*;}
-keep public class android.app.AppOpsManager{*;}
-keep public class android.app.AppOpsManager$PackageOps{*;}
-keep public class android.app.AppOpsManager$OpEntry{*;}
-keep public class android.app.ContentProviderHolder{*;}
-keep public class android.app.IActivityManager{*;}
-keep public class android.app.IActivityManager$ContentProviderHolder{*;}
-keep public class android.app.IActivityManager$Stub{*;}
-keep public class android.app.IProcessObserver{*;}
-keep public class android.app.IProcessObserver$Stub{*;}

-keep public class android.content.pm.IPackageManager{*;}
-keep public class android.content.pm.IPackageManager$Stub{*;}
-keep public class android.content.pm.ParceledListSlice{*;}
-keep public class android.content.pm.UserInfo{*;}
-keep public class android.content.IContentProvider{*;}

-keep public class android.os.ServiceManager{
    public static android.os.IBinder getService(java.lang.String);
}
-keep public class android.permission.IPermissionManager{*;}
-keep public class android.permission.IPermissionManager$Stub{*;}

-keep public class com.android.internal.app.IAppOpsService{*;}
-keep public class com.android.internal.app.IAppOpsService$Stub{*;}