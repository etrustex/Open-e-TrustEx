<?xml version="1.0" encoding="UTF-8"?>
<!--
Refer to EC_CodeList.xls for a mapping of error codes to corresponding human readable values
-->
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
<title>Business rules for e-TrustEx DocumentBundle</title>
<ns prefix="qdt"
      uri="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2"/>
  <ns prefix="cct"
      uri="urn:oasis:names:specification:ubl:schema:xsd:CoreComponentParameters-2"/>
  <ns prefix="cbc"
      uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"/>
  <ns prefix="cac"
      uri="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"/>
  <ns prefix="udt"
      uri="urn:un:unece:uncefact:data:draft:UnqualifiedDataTypesSchemaModule:2"/>
  <ns prefix="stat"
      uri="urn:oasis:names:specification:ubl:schema:xsd:DocumentStatusCode-1.0"/>
  <ns prefix="ec"
      uri="ec:schema:xsd:DocumentBundle-0.1"/>
  <ns prefix="fn"
      uri="http://www.w3.org/2005/xpath-functions"/>
  <ns prefix="xs"
      uri="http://www.w3.org/2001/XMLSchema"/>
<pattern name="date check">
  <rule context="*[local-name()='DocumentBundle']/cbc:IssueDate">
    <assert flag="warning" test="not ( fn:days-from-duration(xs:date(fn:current-date()) - xs:date(.)) &gt; 365 )">error.issuedate_old</assert>
    <assert flag="warning" test="not ( fn:days-from-duration(xs:date(fn:current-date()) - xs:date(.)) &lt; 0 )">error.issuedate_future</assert>
  </rule>
</pattern>
<pattern name="parent document id check">
  <rule context="*[local-name()='DocumentBundle']/cbc:ParentDocumentID">
    <let name="apos" value='"&apos;"'/>
    <assert flag="error" test="not ( string-length(normalize-space(.)) &gt; 250 )">error.parent_document_id_length_check</assert>
    <assert flag="error" test="not ( translate(translate(., ' !&quot;&quot;#$%&amp;()*+,-./0123456789:;&lt;=&gt;?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_`abcdefghijklmnopqrstuvwxyz{{|}}~', ''), $apos, '') != '') ">error.parent_document_id_invalid_char</assert>
  </rule>
</pattern>
    <include href="DocumentBundle_code_list_constraints.sch"/>
</schema>
  
