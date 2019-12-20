#!/bin/bash

# run the main java file
java -prism.order=sw -cp bin/java/ sharp.game.Driver "os: mac" > nul 2> nul

# if there was an error, go to the runerr statement
if errorlevel 1 goto:runerr

goto:EOF

:runerr
echo %prefix% Error!
pause