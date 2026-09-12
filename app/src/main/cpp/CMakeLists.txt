cmake_minimum_required(VERSION 3.22.1)
project("poweros_core")

find_program(CARGO_CMD cargo)

if (CARGO_CMD)
    # Rust NDK integration using Corrosion
    include(FetchContent)
    FetchContent_Declare(
        Corrosion
        GIT_REPOSITORY https://github.com/corrosion-rs/corrosion.git
        GIT_TAG v0.4.7
    )
    FetchContent_MakeAvailable(Corrosion)

    corrosion_import_crate(MANIFEST_PATH ../rust/Cargo.toml)
else()
    # Fallback C++ implementation for environments without Rust toolchain
    add_library(poweros_core SHARED
        poweros_core.cpp
    )

    find_library(log-lib log)

    target_link_libraries(poweros_core
        ${log-lib}
    )
endif()
