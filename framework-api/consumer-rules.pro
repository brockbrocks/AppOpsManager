-keep public class android.app.ActivityThread
-keep public class android.app.IActivityManager{*;}
-keep public class android.app.IActivityManager$Stub{*;}
-keep public class android.app.IProcessObserver{*;}

-keep public class android.content.pm.IPackageManager{*;}
-keep public class android.content.pm.IPackageManager$Stub{*;}
-keep public class android.content.IContentProvider{*;}

-keep public class android.os.ServiceManager{
    public static android.os.IBinder getService(java.lang.String);
}