cmake_minimum_required(VERSION 3.18.1)

project("binder_api")

add_library(
        binder_api

        SHARED

        binder_api.cpp
        binder/binder.cpp
        binder/binder.h
)

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

target_link_libraries( # Specifies the target library.
        binder_api

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})