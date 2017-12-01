<?xml version="1.0" encoding="UTF-8"?>
<!--
Refer to EC_CodeList.xls for a mapping of error codes to corresponding human readable values
-->
<schema xmlns="http://purl.oclc.org/dsdl/schematron">
	<title>Business rules for e-TrustEx CreateParty version 2.1</title>
	<ns prefix="qdt" uri="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2"/>
	<ns prefix="cct" uri="urn:oasis:names:specification:ubl:schema:xsd:CoreComponentParameters-2"/>
	<ns prefix="cbc" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2"/>
	<ns prefix="cac" uri="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2"/>
	<ns prefix="udt" uri="urn:un:unece:uncefact:data:draft:UnqualifiedDataTypesSchemaModule:2"/>
	<ns prefix="stat" uri="urn:oasis:names:specification:ubl:schema:xsd:DocumentStatusCode-1.0"/>
	<ns prefix="ec" uri="ec:schema:xsd:CreateParty-2"/>
	<ns prefix="fn" uri="http://www.w3.org/2005/xpath-functions"/>
	<ns prefix="xs" uri="http://www.w3.org/2001/XMLSchema"/>
	<pattern name="party name exists">
	  <rule context="*[local-name()='CreateParty']/cac:Party">
		<assert flag="error" test="fn:count(cac:PartyName) = 1">Party must contain exactly one PartyName</assert>
		<assert flag="error" test="fn:count(cac:PartyIdentification) &gt;= 1">Party must contain at least one PartyIdentification</assert>
	  </rule>	  	
	  <rule context="*[local-name()='CreateParty']/cac:Party/cac:PartyName/cbc:Name">
		<assert flag="error" test="not(fn:normalize-space(.) = '')">Party name is empty</assert>
		<assert flag="error" test="not(string-length(normalize-space(.)) &gt; 255)">Party name must be at most 255 characters long</assert>
	  </rule>	    
	  <rule context="*[local-name()='CreateParty']/cac:Party/cac:PartyIdentification/cbc:ID">
		<assert flag="error" test="not(fn:normalize-space(.) = '')">Party identifier is empty</assert>
		<assert flag="error" test="not(string-length(normalize-space(.)) &gt; 255)">Party identifier must be at most 255 characters long</assert>
		<assert flag="error" test="not(string-length(normalize-space(@schemeID)) &gt; 255)">Scheme ID must be at most 255 characters long</assert>
	  </rule>	    
	  
	</pattern>
	<include href="CreateParty_2_1_code_list_constraints.sch"/>
</schema>
  
