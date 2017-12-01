@echo off

rem  Copyright 2008-2009 PwC for the e-PRIOR project at the European Commission (DIGIT)
rem  Procedure based on a sample designed by G. Ken Holman

if not exist ..\utility\ContextValueAssociation.xsd goto :nofile

echo Precondition validation...
echo.

echo ----------------------------------------------------------------------------------------------------
echo Validating code-list constraints
echo ----------------------------------------------------------------------------------------------------
echo Validating partner-agreed constraints...
echo   w3cschema ContextValueAssociation.xsd CreateInterchangeAgreement_2_1_constraints.cva
call ..\utility\w3cschema ..\utility\ContextValueAssociation.xsd CreateInterchangeAgreement_2_1_constraints.cva 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo Validating partner-agreed constraints...
echo   w3cschema ContextValueAssociation.xsd CreateParty_2_1_constraints.cva
call ..\utility\w3cschema ..\utility\ContextValueAssociation.xsd CreateParty_2_1_constraints.cva 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo Validating partner-agreed constraints...
echo   w3cschema ContextValueAssociation.xsd DeleteDocumentWrapper_constraints.cva
call ..\utility\w3cschema ..\utility\ContextValueAssociation.xsd DeleteDocumentWrapper_constraints.cva 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo Validating partner-agreed constraints...
echo   w3cschema ContextValueAssociation.xsd DocumentBundle_constraints.cva
call ..\utility\w3cschema ..\utility\ContextValueAssociation.xsd DocumentBundle_constraints.cva 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo Validating partner-agreed constraints...
echo   w3cschema ContextValueAssociation.xsd QueryRequest_constraints.cva
call ..\utility\w3cschema ..\utility\ContextValueAssociation.xsd QueryRequest_constraints.cva 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo Validating partner-agreed constraints...
echo   w3cschema ContextValueAssociation.xsd RetrieveDocumentWrapperRequest_constraints.cva
call ..\utility\w3cschema ..\utility\ContextValueAssociation.xsd RetrieveDocumentWrapperRequest_constraints.cva 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo Validating partner-agreed constraints...
echo   w3cschema ContextValueAssociation.xsd RetrieveRequest_constraints.cva
call ..\utility\w3cschema ..\utility\ContextValueAssociation.xsd RetrieveRequest_constraints.cva 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo Validating partner-agreed constraints...
echo   w3cschema ContextValueAssociation.xsd StatusRequest_constraints.cva
call ..\utility\w3cschema ..\utility\ContextValueAssociation.xsd StatusRequest_constraints.cva 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo Validating partner-agreed constraints...
echo   w3cschema ContextValueAssociation.xsd StoreDocumentWrapper_constraints.cva
call ..\utility\w3cschema ..\utility\ContextValueAssociation.xsd StoreDocumentWrapper_constraints.cva 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo Validating partner-agreed constraints...
echo   w3cschema ContextValueAssociation.xsd SubmitApplicationResponse_constraints.cva
call ..\utility\w3cschema ..\utility\ContextValueAssociation.xsd SubmitApplicationResponse_constraints.cva 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo Validating partner-agreed constraints...
echo   w3cschema ContextValueAssociation.xsd ViewRequest_constraints.cva
call ..\utility\w3cschema ..\utility\ContextValueAssociation.xsd ViewRequest_constraints.cva 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt


echo ----------------------------------------------------------------------------------------------------
echo Validating code lists
echo ----------------------------------------------------------------------------------------------------
echo   w3cschema genericode.xsd DocumentTypeCode.gc
call ..\utility\w3cschema ..\utility\genericode.xsd DocumentTypeCode.gc 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt
echo   w3cschema genericode.xsd ParentDocumentTypeCode.gc
call ..\utility\w3cschema ..\utility\genericode.xsd ParentDocumentTypeCode.gc 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt
echo   w3cschema genericode.xsd ResponseCode.gc
call ..\utility\w3cschema ..\utility\genericode.xsd ResponseCode.gc 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt
echo   w3cschema genericode.xsd RoleCode.gc
call ..\utility\w3cschema ..\utility\genericode.xsd RoleCode.gc 2>&1 >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for DeleteDocumentWrapper
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt DeleteDocumentWrapper_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      DeleteDocumentWrapper_code_list_constraints.sch
call ..\utility\xslt DeleteDocumentWrapper_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl DeleteDocumentWrapper_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Code list rules with business rules
echo.
echo Assembling rules into a Schematron schema...
echo   xslt DeleteDocumentWrapper_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      DeleteDocumentWrapper.sch
call ..\utility\xslt DeleteDocumentWrapper_total_constraints.sch ..\utility\iso_schematron_assembly.xsl DeleteDocumentWrapper.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt DeleteDocumentWrapper.sch ..\utility\iso_svrl.xsl
echo                                                      DeleteDocumentWrapper.xsl
call ..\utility\xslt DeleteDocumentWrapper.sch ..\utility\iso_svrl.xsl DeleteDocumentWrapper.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt


echo ----------------------------------------------------------------------------------------------------
echo Generating files for DocumentBundle
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt DocumentBundle_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      DocumentBundle_code_list_constraints.sch
call ..\utility\xslt DocumentBundle_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl DocumentBundle_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Code list rules with business rules
echo.
echo Assembling rules into a Schematron schema...
echo   xslt DocumentBundle_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      DocumentBundle.sch
call ..\utility\xslt DocumentBundle_total_constraints.sch ..\utility\iso_schematron_assembly.xsl DocumentBundle.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt DocumentBundle.sch ..\utility\iso_svrl.xsl
echo                                                      DocumentBundle.xsl
call ..\utility\xslt DocumentBundle.sch ..\utility\iso_svrl.xsl DocumentBundle.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for RetrieveDocumentWrapperRequest
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt RetrieveDocumentWrapperRequest_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      RetrieveDocumentWrapperRequest_code_list_constraints.sch
call ..\utility\xslt RetrieveDocumentWrapperRequest_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl RetrieveDocumentWrapperRequest_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Code list rules with business rules
echo.
echo Assembling rules into a Schematron schema...
echo   xslt RetrieveDocumentWrapperRequest_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      RetrieveDocumentWrapperRequest.sch
call ..\utility\xslt RetrieveDocumentWrapperRequest_total_constraints.sch ..\utility\iso_schematron_assembly.xsl RetrieveDocumentWrapperRequest.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt RetrieveDocumentWrapperRequest.sch ..\utility\iso_svrl.xsl
echo                                                      RetrieveDocumentWrapperRequest.xsl
call ..\utility\xslt RetrieveDocumentWrapperRequest.sch ..\utility\iso_svrl.xsl RetrieveDocumentWrapperRequest.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for RetrieveRequest
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt RetrieveRequest_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      RetrieveRequest_code_list_constraints.sch
call ..\utility\xslt RetrieveRequest_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl RetrieveRequest_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Code list rules with business rules
echo.
echo Assembling rules into a Schematron schema...
echo   xslt RetrieveRequest_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      RetrieveRequest.sch
call ..\utility\xslt RetrieveRequest_total_constraints.sch ..\utility\iso_schematron_assembly.xsl RetrieveRequest.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt RetrieveRequest.sch ..\utility\iso_svrl.xsl
echo                                                      RetrieveRequest.xsl
call ..\utility\xslt RetrieveRequest.sch ..\utility\iso_svrl.xsl RetrieveRequest.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for StatusRequest
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt StatusRequest_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      StatusRequest_code_list_constraints.sch
call ..\utility\xslt StatusRequest_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl StatusRequest_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Code list rules with business rules
echo.
echo Assembling rules into a Schematron schema...
echo   xslt StatusRequest_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      StatusRequest.sch
call ..\utility\xslt StatusRequest_total_constraints.sch ..\utility\iso_schematron_assembly.xsl StatusRequest.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt StatusRequest.sch ..\utility\iso_svrl.xsl
echo                                                      StatusRequest.xsl
call ..\utility\xslt StatusRequest.sch ..\utility\iso_svrl.xsl StatusRequest.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for StoreDocumentWrapper
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt StoreDocumentWrapper_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      StoreDocumentWrapper_code_list_constraints.sch
call ..\utility\xslt StoreDocumentWrapper_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl StoreDocumentWrapper_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Code list rules with business rules
echo.
echo Assembling rules into a Schematron schema...
echo   xslt StoreDocumentWrapper_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      StoreDocumentWrapper.sch
call ..\utility\xslt StoreDocumentWrapper_total_constraints.sch ..\utility\iso_schematron_assembly.xsl StoreDocumentWrapper.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt StoreDocumentWrapper.sch ..\utility\iso_svrl.xsl
echo                                                      StoreDocumentWrapper.xsl
call ..\utility\xslt StoreDocumentWrapper.sch ..\utility\iso_svrl.xsl StoreDocumentWrapper.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for SubmitApplicationResponse
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt SubmitApplicationResponse_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      SubmitApplicationResponse_code_list_constraints.sch
call ..\utility\xslt SubmitApplicationResponse_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl SubmitApplicationResponse_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Code list rules with business rules
echo.
echo Assembling rules into a Schematron schema...
echo   xslt SubmitApplicationResponse_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      SubmitApplicationResponse.sch
call ..\utility\xslt SubmitApplicationResponse_total_constraints.sch ..\utility\iso_schematron_assembly.xsl SubmitApplicationResponse.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt SubmitApplicationResponse.sch ..\utility\iso_svrl.xsl
echo                                                      SubmitApplicationResponse.xsl
call ..\utility\xslt SubmitApplicationResponse.sch ..\utility\iso_svrl.xsl SubmitApplicationResponse.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for QueryRequest
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt QueryRequest_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      QueryRequest_code_list_constraints.sch
call ..\utility\xslt QueryRequest_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl QueryRequest_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Code list rules with business rules
echo.
echo Assembling rules into a Schematron schema...
echo   xslt QueryRequest_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      QueryRequest.sch
call ..\utility\xslt QueryRequest_total_constraints.sch ..\utility\iso_schematron_assembly.xsl QueryRequest.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt QueryRequest.sch ..\utility\iso_svrl.xsl
echo                                                      QueryRequest.xsl
call ..\utility\xslt QueryRequest.sch ..\utility\iso_svrl.xsl QueryRequest.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for ViewRequest
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt ViewRequest_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      ViewRequest_code_list_constraints.sch
call ..\utility\xslt ViewRequest_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl ViewRequest_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Code list rules with business rules
echo.
echo Assembling rules into a Schematron schema...
echo   xslt ViewRequest_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      ViewRequest.sch
call ..\utility\xslt ViewRequest_total_constraints.sch ..\utility\iso_schematron_assembly.xsl ViewRequest.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt ViewRequest.sch ..\utility\iso_svrl.xsl
echo                                                      ViewRequest.xsl
call ..\utility\xslt ViewRequest.sch ..\utility\iso_svrl.xsl ViewRequest.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for DocumentBundleJustice
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt DocumentBundleJustice_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      DocumentBundleJustice_code_list_constraints.sch
call ..\utility\xslt DocumentBundleJustice_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl DocumentBundleJustice_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Code list rules with business rules
echo.
echo Assembling rules into a Schematron schema...
echo   xslt DocumentBundleJustice_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      DocumentBundleJustice.sch
call ..\utility\xslt DocumentBundleJustice_total_constraints.sch ..\utility\iso_schematron_assembly.xsl DocumentBundleJustice.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt DocumentBundleJustice.sch ..\utility\iso_svrl.xsl
echo                                                      DocumentBundleJustice.xsl
call ..\utility\xslt DocumentBundleJustice.sch ..\utility\iso_svrl.xsl DocumentBundleJustice.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for QueryRequestJustice
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt QueryRequestJustice_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      QueryRequestJustice_code_list_constraints.sch
call ..\utility\xslt QueryRequestJustice_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl QueryRequestJustice_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Code list rules with business rules
echo.
echo Assembling rules into a Schematron schema...
echo   xslt QueryRequestJustice_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      QueryRequestJustice.sch
call ..\utility\xslt QueryRequestJustice_total_constraints.sch ..\utility\iso_schematron_assembly.xsl QueryRequestJustice.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt QueryRequestJustice.sch ..\utility\iso_svrl.xsl
echo                                                      QueryRequestJustice.xsl
call ..\utility\xslt QueryRequestJustice.sch ..\utility\iso_svrl.xsl QueryRequestJustice.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for CreateParty_2_1
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt CreateParty_2_1_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      CreateParty_2_1_code_list_constraints.sch
call ..\utility\xslt CreateParty_2_1_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl CreateParty_2_1_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt CreateParty_2_1_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      CreateParty_2_1.sch
call ..\utility\xslt CreateParty_2_1_total_constraints.sch ..\utility\iso_schematron_assembly.xsl CreateParty_2_1.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt CreateParty_2_1.sch ..\utility\iso_svrl.xsl
echo                                                      CreateParty_2_1.xsl
call ..\utility\xslt CreateParty_2_1.sch ..\utility\iso_svrl.xsl CreateParty_2_1.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo ----------------------------------------------------------------------------------------------------
echo Generating files for CreateInterchangeAgreement_2_1
echo ----------------------------------------------------------------------------------------------------
echo.
echo Preparing code list rules...
echo.
echo Translating partner-agreed constraints into Schematron rules...
echo  xslt CreateInterchangeAgreement_2_1_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl
echo                                                      CreateInterchangeAgreement_2_1_code_list_constraints.sch
call ..\utility\xslt CreateInterchangeAgreement_2_1_constraints.cva ..\utility\Crane-NM-genericode2Schematron.xsl CreateInterchangeAgreement_2_1_code_list_constraints.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt CreateInterchangeAgreement_2_1_total_constraints.sch ..\utility\iso_schematron_assembly.xsl 
echo                                                      CreateInterchangeAgreement_2_1.sch
call ..\utility\xslt CreateInterchangeAgreement_2_1_total_constraints.sch ..\utility\iso_schematron_assembly.xsl CreateInterchangeAgreement_2_1.sch >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Translating Schematron into validation stylesheet...
echo   xslt CreateInterchangeAgreement_2_1.sch ..\utility\iso_svrl.xsl
echo                                                      CreateInterchangeAgreement_2_1.xsl
call ..\utility\xslt CreateInterchangeAgreement_2_1.sch ..\utility\iso_svrl.xsl CreateInterchangeAgreement_2_1.xsl >output.txt
if %errorlevel% neq 0 goto :error
type output.txt

echo.
echo Done.
goto :done

:nofile
echo.
echo Document model for constraints not found: ..\utility\ContextValueAssociation.xsd
goto :done

:error
type output.txt
echo.
echo Error; process terminated!
goto :done

:done
pause