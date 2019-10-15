@echo off

set JSCAF_HOME=E:\JSCAF\trunk\sources\jscaf\tools\..

rem set M2_HOME=%JSCAF_HOME%\tools\apache-maven-3.1.0
set M2=%M2_HOME%\bin
set ANT_HOME=%JSCAF_HOME%\tools\apache-ant-1.9.2
set ANT_BIN=%ANT_HOME%\bin
set CLASSPATH=%ANT_HOME%\lib\*;%ANT_HOME%\etc\ant.jar;
set ANT_OPTS=-Xms512m -Xmx1024m -XX:PermSize=128m  -XX:MaxPermSize=512m 
set PATH=%JAVA_HOME%\bin;%ANT_BIN%;%M2%;%systemroot%\System32;


set JSCAF_VERSION=@jscaf.version@
set APP_NAME_DISPLAY=@app.name.display@
set APP_NAME=@app.name@
set APP_GENERATED_DIR=@app.generated.dir@
set APP_BASE_GENERATED_DIR=@app.generated.dir@-base
