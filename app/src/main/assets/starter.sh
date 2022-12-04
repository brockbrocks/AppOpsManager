#!/bin/sh
package="app.demo.framework"
path=$(pm path $package)
classpath=${path#*"package:"}

LD_LIBRARY_PATH=$LD_LIBRARY_PATH:"$classpath!/lib/$(getprop ro.product.cpu.abi)"

export LD_LIBRARY_PATH
app_process -Djava.class.path="$classpath" /system/bin app.demo.framework.service.ShellService
