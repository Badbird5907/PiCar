@echo off
rem usage: build.bat [arm(a)|windows(w)]
rem Command: ./gradlew -PjavacppPlatform=[linux-arm64|windows-x86_64] build

if "%1"=="arm" (
    set platform=linux-arm64
) else if "%1"=="a" (
    set platform=linux-arm64
) else if "%1"=="windows" (
    set platform=windows-x86_64
) else if "%1"=="w" (
    set platform=windows-x86_64
) else (
    echo Usage: ./build.bat [arm/windows]
    exit /b 1
)

echo Building for %platform%
call gradlew.bat -PjavacppPlatform=%platform% build
