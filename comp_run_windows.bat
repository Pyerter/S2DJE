@echo off

call compile_windows.bat Compilation done. Program will run on continue.
:: call run_windows and pass in run arguments
if errorlevel 1 goto:err
call run_windows.bat "output: false" %*

:err