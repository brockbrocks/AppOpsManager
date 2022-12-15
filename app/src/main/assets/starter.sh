#!/bin/sh
package="app.jhau.appopsmanager"
path=$(pm path $package)
classpath=${path#*"package:"}
sdk_version=$(getprop ro.build.version.sdk)

LD_LIBRARY_PATH=$LD_LIBRARY_PATH:"${classpath}!/lib/$(getprop ro.product.cpu.abi)"

echo "#----------Info-----------"
echo "# PackageName = ${package}"
echo "# PackagePath = ${path}"
echo "# ClassPath = ${classpath}"
echo "# SDK = ${sdk_version}"
echo "#-----------End-----------"

export LD_LIBRARY_PATH
app_process -Djava.class.path="$classpath" /system/bin app.jhau.server.ServerStarter
