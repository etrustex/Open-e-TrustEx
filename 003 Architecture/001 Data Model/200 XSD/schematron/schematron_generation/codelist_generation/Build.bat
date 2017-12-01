@echo off

rem  Copyright PwC for the e-PRIOR project at the European Commission (DIGIT)

echo.
echo Generating code lists...
echo   xslt DocumentTypeCode.xml MappingMapToGenericode.xslt DocumentTypeCode.gc
call ..\utility\xslt DocumentTypeCode.xml MappingMapToGenericode.xslt DocumentTypeCode.gc 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt
echo   xslt ErrorCode.xml MappingMapToGenericode.xslt ErrorCode.gc
call ..\utility\xslt ErrorCode.xml MappingMapToGenericode.xslt ErrorCode.gc 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt
echo   xslt ParentDocumentTypeCode.xml MappingMapToGenericode.xslt ParentDocumentTypeCode.gc
call ..\utility\xslt ParentDocumentTypeCode.xml MappingMapToGenericode.xslt ParentDocumentTypeCode.gc 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt
echo   xslt ResponseCode.xml MappingMapToGenericode.xslt ResponseCode.gc
call ..\utility\xslt ResponseCode.xml MappingMapToGenericode.xslt ResponseCode.gc 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt
echo   xslt SortFieldTypeCode.xml MappingMapToGenericode.xslt SortFieldTypeCode.gc
call ..\utility\xslt SortFieldTypeCode.xml MappingMapToGenericode.xslt SortFieldTypeCode.gc 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt
echo   xslt RoleCode.xml MappingMapToGenericode.xslt RoleCode.gc
call ..\utility\xslt RoleCode.xml MappingMapToGenericode.xslt RoleCode.gc 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Done.
goto :done

:error
type output.txt
echo.
echo Error; process terminated!
goto :done

:done
pause