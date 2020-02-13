@echo off

set prefix=Sharp$
set jdkversion=jdk1.8.0_231
set path=C:\Program Files\Java\%jdkversion%\bin

echo %prefix% Compiling and running Sharp
echo %prefix%
echo %prefix%

:: test compiling before removing bin in case jdk is not installed
echo %prefix% Test compiling interface: src\main\java\sharp\utility\Updatable.java
javac -d .\bin\java\ src\main\java\sharp\utility\Updatable.java > nul 2> nul

:: if it didn't compile, that means jdk is not installed
if errorlevel 1 goto:testcomperr
echo %prefix% Test compile successful.

echo %prefix%

:: clean target directory
echo %prefix% Cleaning target directory: bin\java\sharp\
md bin\java\ 2> nul
rmdir  /s /q bin\java\sharp\ 2> nul

echo %prefix%

:: compile java files
echo %prefix% Compiling java files found under src\main\java...
dir .\src\main\java\sharp\*.java /s /a-d /b /w   > sources.txt
javac -Xlint:unchecked -d .\bin\java\ @sources.txt > compResults.txt 2> compResults2.txt

:: if there was an error go to comperr statement
if errorlevel 1 goto:comperr
echo %prefix% Compilation successful!
pause
goto:EOF

:testcomperr
echo %prefix% Test compile unsuccessful.
echo %prefix%
echo %prefix% Please install the correct jdk of version 8 before compiling.
echo %prefix% The attempted jdk to use is: %jdkversion%
echo %prefix% If you have a jdk 1.8 version, edit this batch file and
echo %prefix% change the jdkversion variable value to match your jdk.
pause
goto:EOF

:comperr
echo %prefix% Compilation unsuccessful
echo %prefix% Check compResults.txt for compile messages
pause
goto:EOF