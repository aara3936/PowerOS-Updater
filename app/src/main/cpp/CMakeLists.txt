cmake_minimum_required(VERSION 3.22.1)

project("nativesecurity" CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_library(
    native-security
    SHARED
    native-security.cpp
)

find_library(
    log-lib
    log
)

target_link_libraries(
    native-security
    ${log-lib}
)
