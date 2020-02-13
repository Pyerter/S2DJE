@echo off

call compile_windows.bat
:: call run_windows and pass in run arguments
call run_windows.bat "output: false"