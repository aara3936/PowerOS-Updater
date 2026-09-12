cmake_minimum_required(VERSION 3.22.1)
project("poweros_core")

include(FetchContent)
FetchContent_Declare(
    mbedtls
    GIT_REPOSITORY https://github.com/Mbed-TLS/mbedtls.git
    GIT_TAG v3.5.0
)
set(ENABLE_PROGRAMS OFF CACHE BOOL "Disable mbedtls programs")
set(ENABLE_TESTING OFF CACHE BOOL "Disable mbedtls tests")
FetchContent_MakeAvailable(mbedtls)

add_library(poweros_core SHARED
    poweros_core.cpp
)

find_library(log-lib log)

target_link_libraries(poweros_core
    PRIVATE
    mbedcrypto
    ${log-lib}
)
