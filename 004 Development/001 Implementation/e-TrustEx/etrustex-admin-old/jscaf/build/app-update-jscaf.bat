@echo off

call ../tools/setlocal.cmd

copy %JSCAF_HOME%\build\app-create-page.bat .
copy %JSCAF_HOME%\build\app-update-jscaf.bat .
copy build.xml build.xml.old
copy %JSCAF_HOME%\build\build.xml .
copy build.properties build.properties.old
copy %JSCAF_HOME%\build\build.properties .
copy %JSCAF_HOME%\build\build.definitions.xml .

call ../tools/ant app.update.jscaf  -Dsvn.jscaf.local.dir=%JSCAF_HOME%\sources\jscaf  -Dapp.properties.file=build.app.properties -Djscaf.home.dir=%JSCAF_HOME%

