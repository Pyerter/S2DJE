@echo off

:: run the main java file
java -cp bin\java\ sharp.game.Driver "output: false"

:: if there was an error, go to the runerr statement
if errorlevel 1 goto:runerr

goto:EOF

:runerr
echo %prefix% Error!
pause
goto:EOF
