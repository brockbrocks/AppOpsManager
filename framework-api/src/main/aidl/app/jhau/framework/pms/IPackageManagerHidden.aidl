// IPackageManagerHidden.aidl
package app.jhau.framework.pms;

interface IPackageManagerHidden {
    List<PackageInfo> getInstalledPackages(int flags, int userId);
    List<PackageInfo> getInstalledPackagesApi33(long flags, int userId);
}