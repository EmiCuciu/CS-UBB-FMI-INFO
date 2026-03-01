cmake_minimum_required(VERSION 4.0)
project(lab_in_class_s6)

set(CMAKE_CXX_STANDARD 20)

find_package(MPI REQUIRED)
include_directories(${MPI_INCLUDE_PATH})


add_executable(lab_in_class_s6 main.cpp)
target_link_libraries(lab_in_class_s6 ${MPI_LIBRARIES})

if (MPI_COMPILE_FLAGS)
  set_target_properties(lab_in_class_s6 PROPERTIES
    COMPILE_FLAGS "${MPI_COMPILE_FLAGS}")
endif ()

if (MPI_LINK_FLAGS)
  set_target_properties(lab_in_class_s6 PROPERTIES
    LINK_FLAGS "${MPI_LINK_FLAGS}")
endif ()