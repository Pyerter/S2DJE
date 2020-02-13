@echo off

set prefix=Sharp$
set jdkversion=jdk1.8.0_231
set path=C:\Program Files\Java\%jdkversion%\bin

:: run the main java file
echo %prefix% Running sharp.game.Driver (launch App)!
:: all arguments given to this file are passed into the java driver
java -cp bin\java\ sharp.game.Driver %* 2> javaOutFile.txt

:: if there was an error, go to the runerr statement
if errorlevel 1 goto:runerr
echo %prefix% Runtime successful!

echo %prefix%

pause
goto:EOF

:runerr
echo %prefix% Runtime unsuccessful
pause
goto:EOF