cmake_minimum_required(VERSION 3.22.1)
project("poweros_core")

add_library(poweros_core SHARED
    poweros_core.cpp
)

find_library(log-lib log)

target_link_libraries(poweros_core
    ${log-lib}
)
