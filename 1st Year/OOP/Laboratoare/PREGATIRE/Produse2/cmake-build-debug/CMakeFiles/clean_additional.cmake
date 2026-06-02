# Additional clean files
cmake_minimum_required(VERSION 3.16)

if("${CONFIG}" STREQUAL "" OR "${CONFIG}" STREQUAL "Debug")
  file(REMOVE_RECURSE
  "CMakeFiles\\Produse2_autogen.dir\\AutogenUsed.txt"
  "CMakeFiles\\Produse2_autogen.dir\\ParseCache.txt"
  "Produse2_autogen"
  )
endif()
