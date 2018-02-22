@ECHO OFF
start "" RLBot_Injector.exe
ECHO Injecting dll!
start cmd /K gradlew.bat run
ECHO Starting gradlew/java grpc!
PAUSE
start cmd /K python runner.py
ECHO Game started!