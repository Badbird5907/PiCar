@echo off

setlocal

set SKIP_BUILD=false

REM Check for the deploy-only option
if "%1" == "-do" (
  set SKIP_BUILD=true
  shift
) else if "%1" == "--deploy-only" (
  set SKIP_BUILD=true
  shift
)

REM Call build.bat if SKIP_BUILD is false
if not "%SKIP_BUILD%" == "true" (
  call build.bat a
)

set LOCAL_FILE_PATH=build\libs\PiCar.jar
set CREDENTIALS_FILE=%userprofile%\.ssh\pi.txt

if not exist "%CREDENTIALS_FILE%" (
  echo Credentials file not found at %CREDENTIALS_FILE%
  exit /b 1
)

for /f "tokens=1,2 delims=:" %%A in (%CREDENTIALS_FILE%) do (
  set PI_USERNAME=%%A
  set PI_PASSWORD=%%B
)

rem the static ip address of the raspberry pi on the router we're going to be using
set PI_HOST=192.168.0.69

rem pscp -pw %PI_PASSWORD% "%LOCAL_FILE_PATH%" "%PI_USERNAME%@%PI_HOST%:/home/pi/"

winscp.com /command "open sftp://%PI_USERNAME%:%PI_PASSWORD%@%PI_HOST%" "put ""%LOCAL_FILE_PATH%"" /home/pi/car/" "exit"

if %errorlevel%==0 (
  echo Copy completed successfully.
) else (
  echo Error occurred during the copy.
)

endlocal
