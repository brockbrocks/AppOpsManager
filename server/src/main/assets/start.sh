#!/system/bin/sh
package=%%%package%%%
classpath=%%%classpath%%%
sdk=%%%sdk%%%
libpath=%%%libpath%%%

echo "#----------Info-----------"
echo "# Package = ${package}"
echo "# ClassPath = ${classpath}"
echo "# LibPath = ${libpath}"
echo "# SDK = ${sdk}"
echo "#-----------End-----------"

export CLASSPATH=$classpath
app_process -Djava.library.path=${libpath}:/system/lib:/vendor/lib /system/bin %%%starterclassname%%%
