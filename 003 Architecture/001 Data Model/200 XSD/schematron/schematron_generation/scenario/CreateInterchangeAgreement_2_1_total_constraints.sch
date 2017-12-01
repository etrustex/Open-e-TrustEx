<?xml version="1.0" encoding="UTF-8"?>
<!--
Refer to EC_CodeList.xls for a mapping of error codes to corresponding human readable values
-->
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
	<title>Business rules for e-TrustEx CreateInterchangeAgreement 2.1</title>
	<ns prefix="qdt" uri="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2"/>
	<ns prefix="cct" uri="urn:oasis:names:specification:ubl:schema:xsd:CoreComponentParameters-2"/>
	<ns prefix="cbc" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"/>
	<ns prefix="eccac" uri="ec:schema:xsd:CommonAggregateComponents-2_1"/>
	<ns prefix="cac" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"/>
	<ns prefix="udt" uri="urn:un:unece:uncefact:data:draft:UnqualifiedDataTypesSchemaModule:2"/>
	<ns prefix="stat" uri="urn:oasis:names:specification:ubl:schema:xsd:DocumentStatusCode-1.0"/>
	<ns prefix="ec" uri="ec:schema:xsd:CreateInterchangeAgreement-2_1"/>
	<ns prefix="fn" uri="http://www.w3.org/2005/xpath-functions"/>
	<ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
	<pattern name="party name exists">
	  <rule context="*[local-name()='CreateInterchangeAgreement']/eccac:InterchangeAgreement">
		<assert flag="error" test="fn:count(eccac:PartyRole) >= 2">Minimum two party roles must be specified</assert>
	  </rule>	  	
	  <rule context="*[local-name()='CreateInterchangeAgreement']/eccac:InterchangeAgreement/eccac:PartyRole/cbc:RoleCode">
		<assert flag="error" test="not(fn:normalize-space(.) = '')">Role code must not be empty</assert>
	  </rule>	    
	  <rule context="*[local-name()='CreateInterchangeAgreement']/eccac:InterchangeAgreement/eccac:PartyRole/cac:Party">
		<assert flag="error" test="fn:count(cac:PartyName) = 1">Party must contain exactly one PartyName</assert>
	  </rule>	  
	  <rule context="*[local-name()='CreateInterchangeAgreement']/eccac:InterchangeAgreement/cac:ValidityPeriod/cbc:StartDate">
		<assert flag="error" test="xs:date(.) &gt;= fn:current-date()">Start date must not be in the past</assert>
	  </rule>	  
	  <rule context="*[local-name()='CreateInterchangeAgreement']/eccac:InterchangeAgreement/eccac:PartyRole/cac:Party/cac:AgentParty">
		<assert flag="error" test="fn:count(cac:PartyName) = 1">Agent party must contain exactly one PartyName</assert>
	  </rule>		  
	</pattern>
	<include href="CreateInterchangeAgreement_2_1_code_list_constraints.sch"/>
</schema>
  
