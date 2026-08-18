@echo off
REM ============================================================================
REM ECG Dumsor Response Optimizer — Automated Test & Compilation Runner
REM Group 15: Codebility v2.0
REM ============================================================================

echo ============================================================================
echo   Running ECG Dumsor Response Optimizer Test Suite
echo   Group 15: Codebility v2.0
echo ============================================================================

echo.
echo [1/3] Compiling Project with Java 21...
if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\bin\javac.exe" (
    set JAVAC_CMD="C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\bin\javac.exe"
    set JAVA_CMD="C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\bin\java.exe"
) else (
    set JAVAC_CMD=javac
    set JAVA_CMD=java
)

if not exist target\classes mkdir target\classes

%JAVAC_CMD% -d target\classes src\main\java\com\g15\dsa\structures\*.java src\main\java\com\g15\dsa\algorithms\searching\*.java src\main\java\com\g15\dsa\algorithms\sorting\*.java src\main\java\com\g15\dsa\algorithms\graph\*.java src\main\java\com\g15\dsa\algorithms\greedy\*.java src\main\java\com\g15\dsa\algorithms\dp\*.java src\main\java\com\g15\dsa\model\*.java src\main\java\com\g15\dsa\database\*.java src\main\java\com\g15\dsa\dao\*.java src\main\java\com\g15\dsa\experiments\*.java src\main\java\com\g15\dsa\App.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation FAILED!
    exit /b %ERRORLEVEL%
)
echo Compilation successful.

echo.
echo [2/3] Executing Main Application CLI Demonstration...
%JAVA_CMD% -cp target\classes com.g15.dsa.App

echo.
echo [3/3] Running Python Performance Benchmarks & Chart Generation...
python scripts\run_all_benchmarks.py

echo.
echo ============================================================================
echo   All steps completed successfully!
echo ============================================================================
