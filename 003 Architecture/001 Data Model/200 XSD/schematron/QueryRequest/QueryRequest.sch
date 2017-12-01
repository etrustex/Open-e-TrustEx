<?xml version="1.0" encoding="UTF-8"?><!--
Refer to EC_CodeList.xls for a mapping of error codes to corresponding human readable values
--><schema xmlns="http://purl.oclc.org/dsdl/schematron">
<title>Business rules for e-TrustEx Query Request</title>
<ns prefix="qdt" uri="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2"/>
  <ns prefix="cct" uri="urn:oasis:names:specification:ubl:schema:xsd:CoreComponentParameters-2"/>
  <ns prefix="cbc" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"/>
  <ns prefix="cac" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"/>
  <ns prefix="udt" uri="urn:un:unece:uncefact:data:draft:UnqualifiedDataTypesSchemaModule:2"/>
  <ns prefix="stat" uri="urn:oasis:names:specification:ubl:schema:xsd:DocumentStatusCode-1.0"/>
  <ns prefix="ec" uri="ec:schema:xsd:QueryRequest-1"/>
<pattern name="period check">
  <rule context="cac:Period">
    <assert flag="error" test="not ( normalize-space(./cbc:StartDate) = '' )">error.period_check_startdate</assert>
    <assert flag="error" test="not ( normalize-space(./cbc:EndDate) = '' )">error.period_check_enddate</assert>
  </rule>
</pattern>
<pattern name="date check">
  <rule context="cac:Period/cbc:StartDate">
    <let name="startYear" value="number(substring(.,1,4))"/>
    <let name="startMonth" value="number(substring(.,6,2))"/>
    <let name="startDay" value="number(substring(.,9,2))"/>
    <let name="endYear" value="number(substring(../cbc:EndDate,1,4))"/>
    <let name="endMonth" value="number(substring(../cbc:EndDate,6,2))"/>
    <let name="endDay" value="number(substring(../cbc:EndDate,9,2))"/>
    <assert flag="error" test="( $startYear &lt; $endYear ) or ( ( $startYear = $endYear ) and ( $startMonth &lt; $endMonth ) ) or ( ( $startYear = $endYear ) and ( $startMonth = $endMonth ) and ( $startDay &lt;= $endDay ) )">error.invalid_date_range</assert>
    <assert flag="error" test="( $startYear = $endYear ) or ( ( $startYear &gt;= ($endYear - 1) ) and ( $startMonth &gt; $endMonth ) ) or ( ( $startYear &gt;= ($endYear - 1) ) and ( $startMonth = $endMonth ) and ( $startDay &gt;= $endDay ) )">error.large_date_range</assert>
  </rule>
</pattern>
    <pattern id="QueryRequest_code_list_rules">
<!--
  This implementation supports genericode code lists with no instance
  meta data.
-->
<!--
    Start of synthesis of rules from code list context associations.

    QueryRequest code list rules for the e-TrustEx project at the European Commission (DIGIT) by PwC
  
Version 0.1
-->
<!--
    End of synthesis of rules from code list context associations.
-->
</pattern>
</schema>