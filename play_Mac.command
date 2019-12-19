@echo off

SET prefix=Sharp$

:: run the main java file
echo %prefix% Running sharp.game.Driver (launch App)!
java  -prism.order=sw -cp bin/java/ sharp.game.Driver

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
