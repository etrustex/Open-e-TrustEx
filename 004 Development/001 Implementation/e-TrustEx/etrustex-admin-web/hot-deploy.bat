@echo off

SETLOCAL

SET PATH=%systemroot%\System32

echo %PATH%



SET WEBAPP_SOURCE=C:\Projects\JSCAF\tags\v1.6\sources\jscaf
SET WEBAPP_TARGET=C:\Pgm\dev\app\servers\WL_12.1.3\user_projects\domains\base_domain\servers\AdminServer\tmp\_WL_user\_appsdir_etrustex-admin-web_war\c24tui\war

call xcopy %WEBAPP_SOURCE%\*.xml %WEBAPP_TARGET%\ /S /Y /D
call xcopy %WEBAPP_SOURCE%\*.html %WEBAPP_TARGET%\ /S /Y /D
call xcopy %WEBAPP_SOURCE%\*.tag %WEBAPP_TARGET%\ /S /Y /D
call xcopy %WEBAPP_SOURCE%\*.jsp %WEBAPP_TARGET%\ /S /Y /D
call xcopy %WEBAPP_SOURCE%\*.jspf %WEBAPP_TARGET%\ /S /Y /D
call xcopy %WEBAPP_SOURCE%\*.js %WEBAPP_TARGET%\ /S /Y /D
call xcopy %WEBAPP_SOURCE%\*.css %WEBAPP_TARGET%\ /S /Y /D

ENDLOCAL