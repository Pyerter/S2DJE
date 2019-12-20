@echo off

SET prefix=Sharp$
set path=C:\Program Files\Java\jdk1.8.0_231\bin

echo %prefix% Compiling and running Sharp
echo %prefix%
echo %prefix%

:: clean target directory
echo %prefix% Cleaning target directory: bin\java\sharp\
md bin\java\ 2> nul
rmdir  /s /q bin\java\sharp\ 2> nul

echo %prefix%

:: compile java files
echo %prefix% Compiling java files found under src\main\java...
dir .\src\main\java\sharp\*.java /s /a-d /b /w   > sources.txt
javac -d .\bin\java\ @sources.txt 2> compResults.txt

:: if there was an error go to comperr statement
if errorlevel 1 goto:comperr
echo %prefix% Compilation successful!

echo %prefix%

:: run the main java file
echo %prefix% Running sharp.game.Driver (launch App)!
java -cp bin\java\ sharp.game.Driver 2> javaOutFile.txt

:: if there was an error, go to the runerr statement
if errorlevel 1 goto:runerr
echo %prefix% Runtime successful!

echo %prefix%

pause
goto:EOF

:comperr
echo %prefix% Compilation unsuccessful
echo %prefix% Check compResults.txt for compile messages
pause
goto:EOF

:runerr
echo %prefix% Runtime unsuccessful
pause
goto:EOF
