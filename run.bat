@echo off

set path=C:\Program Files\Java\jdk1.8.0_231\bin

echo Compiling and running Sharp

:: compile java files
echo Compiling java files..
echo utility\*.java, collision\*.java, game\*.java, unit\*.java
dir .\src\main\java\sharp\*.java /s /a-d /b /w   > sources.txt
javac -d .\bin\ @sources.txt

:: if there was an erro go to err statement and end file
if errorlevel 1 goto:comperr
echo Compilation successful!

pause
goto:EOF

:comperr
echo Compilation unsuccessful
pause
