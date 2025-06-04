@echo off
REM Membuat folder bin jika belum ada
if not exist bin mkdir bin

echo [1] Compiling source code...
javac -d bin -sourcepath src src\AppLauncher.java

if errorlevel 1 (
    echo ❌ Compilation failed.
    pause
    exit /b
)

echo [2] Running game...
java -cp "bin;lib/mysql-connector-j-9.3.0.jar" AppLauncher
pause
