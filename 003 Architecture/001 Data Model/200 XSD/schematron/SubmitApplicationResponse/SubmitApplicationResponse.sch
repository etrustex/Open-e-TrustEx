<?xml version="1.0" encoding="UTF-8"?><!--
Refer to EC_CodeList.xls for a mapping of error codes to corresponding human readable values
--><schema xmlns="http://purl.oclc.org/dsdl/schematron">
<title>Business rules for e-TrustEx SubmitApplicationResponse</title>
<ns prefix="qdt" uri="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2"/>
  <ns prefix="cct" uri="urn:oasis:names:specification:ubl:schema:xsd:CoreComponentParameters-2"/>
  <ns prefix="cbc" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"/>
  <ns prefix="cac" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"/>
  <ns prefix="udt" uri="urn:un:unece:uncefact:data:draft:UnqualifiedDataTypesSchemaModule:2"/>
  <ns prefix="stat" uri="urn:oasis:names:specification:ubl:schema:xsd:DocumentStatusCode-1.0"/>
  <ns prefix="apr" uri="urn:oasis:names:specification:ubl:schema:xsd:ApplicationResponse-2"/>
  <ns prefix="fn" uri="http://www.w3.org/2005/xpath-functions"/>
  <ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
<pattern name="parent document id check">
  <rule context="*[local-name()='ApplicationResponse']/cac:DocumentResponse/cac:DocumentReference/cbc:ID">
    <let name="apos" value="&#34;'&#34;"/>
    <assert flag="error" test="not ( normalize-space(.) = '' )">error.id_check</assert>
    <assert flag="error" test="not ( string-length(normalize-space(.)) &gt; 250 )">error.parent_document_id_length_check</assert>
    <assert flag="error" test="not ( translate(translate(., ' !&#34;&#34;#$%&amp;()*+,-./0123456789:;&lt;=&gt;?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{{|}}~', ''), $apos, '') != '') ">error.parent_document_id_invalid_char</assert>
  </rule>
</pattern>
<pattern name="document reference check">
  <rule context="cac:DocumentReference">
    <assert flag="error" test="not ( normalize-space(cbc:DocumentTypeCode) = '' )">error.parent_document_documenttypecode_check</assert>
    <assert flag="error" test="not ( normalize-space(cbc:DocumentTypeCode) = '301' )">error.parent_document_documenttypecode_check_application_response</assert>
  </rule>
</pattern>
    <pattern id="SubmitApplicationResponse_code_list_rules">
<!--
  This implementation supports genericode code lists with no instance
  meta data.
-->
<!--
    Start of synthesis of rules from code list context associations.

    SubmitApplicationResponse code list rules for the e-TrustEx project at the European Commission (DIGIT) by PwC
  
Version 0.1
-->
<!--
    End of synthesis of rules from code list context associations.
-->
</pattern>
</schema>