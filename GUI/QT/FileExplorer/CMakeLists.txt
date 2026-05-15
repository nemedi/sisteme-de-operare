cmake_minimum_required(VERSION 3.16)

project(FileExplorer)

set(CMAKE_CXX_STANDARD 17)

find_package(Qt6 REQUIRED COMPONENTS Widgets)

qt_standard_project_setup()

qt_add_executable(FileExplorer
    main.cpp
)

target_link_libraries(FileExplorer
    PRIVATE Qt6::Widgets
)
