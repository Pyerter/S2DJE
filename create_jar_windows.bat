@echo off

set prefix=cs1302-arcade$
:: if you don't have java added to environmental variables, uncomment these lines and correct the jdk version
:: to add to environmental variables, search in the search bar and go to Environmental Variables > System > Path > new
:: and typ in the path file to the jdk version bin
:: set jdkversion=jdk1.8.0_231
:: set path=C:\Program Files\Java\%jdkversion%\bin

echo %prefix% Compiling java files
echo %prefix%
echo %prefix%

:: create jar
echo %prefix% Creating jar file
:: dir .\bin\java\sharp /s /a-d /b /w > sourcesJar.txt
jar cvfe s2dje.jar sharp.game.Driver Engine_Demo -C bin\java\ .\sharp

:: if there was an error go to comperr statement
if errorlevel 1 goto:comperr
echo %prefix% Jar build successful!
echo %prefix% %*
pause
goto:EOF

:comperr
echo %prefix% Jar build unsuccessful
pause
goto:EOF