
package app.jhau.framework.permission;

interface IPermissionManagerHidden {
    int getPermissionFlags(String permName, String packageName, int userId);
    int getPermissionFlagsApi33(String packageName, String permissionName, int userId);

    void grantRuntimePermission(String packageName, String permissionName, int userId);
    void revokeRuntimePermission(String packageName, String permissionName, int userId, String reason);
}