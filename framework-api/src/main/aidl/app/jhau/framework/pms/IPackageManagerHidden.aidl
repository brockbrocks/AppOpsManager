// IPackageManagerHidden.aidl
package app.jhau.framework.pms;

interface IPackageManagerHidden {
    List<PackageInfo> getInstalledPackages(long flags, int userId);
}