start "" RLBot_Injector.exe
echo "dll injected!"
SLEEP 4
start cmd /K gradlew.bat run
echo "java started!"
SLEEP 1
start cmd /K python runner.py
echo "Bot started!"