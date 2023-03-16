// IPackageManagerHidden.aidl
package app.jhau.framework.pms;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

interface IPackageManagerHidden {
    ApplicationInfo getApplicationInfo(String packageName, int flags ,int userId);
    ApplicationInfo getApplicationInfoApi33(String packageName, long flags ,int userId);
    List<PackageInfo> getInstalledPackages(int flags, int userId);
    List<PackageInfo> getInstalledPackagesApi33(long flags, int userId);
    PackageInfo getPackageInfo(String packageName, int flags, int userId);
    PackageInfo getPackageInfoApi33(String packageName, long flags, int userId);
    int getPermissionFlags(String permissionName, String packageName, int userId); //deprecatedsince R
    void grantRuntimePermission(String packageName, String permissionName, int userId);
    boolean isPackageAvailable(String packageName, int userId);
    List<ResolveInfo> queryIntentActivities(in Intent intent, String resolvedType, int flags, int userId);
    List<ResolveInfo> queryIntentActivitiesApi33(in Intent intent, String resolvedType, long flags, int userId);
    void revokeRuntimePermission(String packageName, String permissionName, int userId);
    void setApplicationEnabledSetting(in String packageName, in int newState, int flags, int userId, String callingPackage);
}