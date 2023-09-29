@echo off
setlocal

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

set PI_HOST=raspberrypi.local

rem pscp -pw %PI_PASSWORD% "%LOCAL_FILE_PATH%" "%PI_USERNAME%@%PI_HOST%:/home/pi/"

winscp.com /command "open sftp://%PI_USERNAME%:%PI_PASSWORD%@%PI_HOST%" "put ""%LOCAL_FILE_PATH%"" /home/pi/" "exit"


if %errorlevel%==0 (
  echo Copy completed successfully.
) else (
  echo Error occurred during the copy.
)

endlocal
