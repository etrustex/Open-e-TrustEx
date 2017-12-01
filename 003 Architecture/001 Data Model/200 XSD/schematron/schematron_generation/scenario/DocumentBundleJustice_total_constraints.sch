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
  <ns prefix="ec"
      uri="ec:schema:xsd:DocumentBundleJustice-1"/>
  <ns prefix="eccac"
      uri="ec:schema:xsd:CommonAggregateComponents-2"/>
  <ns prefix="eccbc"
      uri="ec:schema:xsd:CommonBasicComponents-1"/>
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
<pattern name="userID check">
  <rule context="*[local-name()='DocumentBundle']">
    <assert flag="error" test="not ( normalize-space(./eccac:UserID) = '' )">error.userid_check</assert>
  </rule>
</pattern>
<pattern name="correlationID check">
  <rule context="*[local-name()='DocumentBundle']">
    <assert flag="error" test="not ( normalize-space(./cac:OriginatorDocumentReference/cbc:ID) = '' )">error.correlationid_check</assert>
  </rule>
</pattern>
<pattern name="contactID check">
  <rule context="cac:ReceiverParty">
    <assert flag="error" test="not ( normalize-space(./cac:Contact/cbc:ID) = '' )">error.contactid_check</assert>
  </rule>
</pattern>
<pattern name="countrySubentityCode check">
  <rule context="cac:ReceiverParty">
    <assert flag="error" test="not ( normalize-space(./cac:PhysicalLocation/cbc:CountrySubentityCode) = '' )">error.countrysubentitycode_check</assert>
  </rule>
</pattern>
    <include href="DocumentBundleJustice_code_list_constraints.sch"/>
</schema>
  
