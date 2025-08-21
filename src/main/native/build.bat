@echo off
if not exist build mkdir build
x86_64-w64-mingw32-g++ -shared -o build/RaidClient.dll RaidClient.cpp -lgdi32 -luser32
