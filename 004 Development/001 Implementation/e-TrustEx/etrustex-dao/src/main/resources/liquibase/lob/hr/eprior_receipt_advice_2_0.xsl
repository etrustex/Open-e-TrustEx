<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cac="urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2" 
                xmlns:cbc="urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2" 
                xmlns:ccts="urn:un:unece:uncefact:documentation:2" xmlns:clm5639="urn:un:unece:uncefact:codelist:specification:5639:1988" xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2"
                xmlns:receiptExt="urn:assets:schema:extensions:receipt"
xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:mdrcp="urn:oasis:names:specification:ubl:schema:xsd:ReceiptAdvice-2" xmlns:n1="ec:schema:xsd:ReceiptAdviceReceived-0.1" xmlns:qdt="urn:oasis:names:specification:ubl:schema:xsd:QualifiedDatatypes-2" xmlns:udt="urn:un:unece:uncefact:data:specification:UnqualifiedDataTypesSchemaModule:2" xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<xsl:output version="4.0" method="html" indent="no" encoding="UTF-8" doctype-public="-//W3C//DTD HTML 4.0 Transitional//EN" doctype-system="http://www.w3.org/TR/html4/loose.dtd"/>
	<xsl:param name="SV_OutputFormat" select="'HTML'"/>
	<xsl:decimal-format name="format1" grouping-separator="." decimal-separator=","/>
	<xsl:template match="/">
    <!-- ================================================= -->
    <!-- Please update TemplateVersion for each deployment -->
    <!-- ================================================= -->
    <xsl:variable name="TemplateVersion" select="213" />
    <!-- ================================================= -->
    
		<html>
			<head>
				<title>Receipt Advice Human Readable Format</title>
				<style type="text/css">
          body {
          font-family: "Liberation Sans";
          font-size: small;
          size: landscape;
          }

          table {
          font-family: "Liberation Sans";
          font-size: medium;
          border-right: none;
          border-left: none;
          border-top: none;
          border-bottom: none;
          }

          table.border {
          border:2px solid black;
          }

          table.noborder {
          border:none 0px black;
          }

          .cellNoBorder{
          border-style:none;
          border-left-style:none;
          border-top-style:none;
          border-right-style:none;
          border-bottom-style:none;
          }

          .cellLeftRightBorder {
          border-left-style:thin;
          border-left-width:1px;
          border-left-color:black;
          border-right-style:thin;
          border-right-width:1px;
          border-right-color:black;
          border-top-style:none;
          border-bottom-style:none;
          }

          .cellTopBorder {
          border-left-style:none;
          border-bottom-style:none;
          border-right-style:none;
          border-top-style:thin;
          border-top-width:1px;
          border-top-color:black;
          }


          @page {
          size: A4 landscape;
          margin-top: 22%;
          margin-bottom: 5%;

          @top-center {
          content: element(header);
          }

          @bottom-left {
          font-family: "Liberation Sans";
          font-size: medium;
          content: "v.<xsl:value-of select="$TemplateVersion" />";
          }

          @bottom-right {
          font-family: "Liberation Sans";
          font-size: medium;
          content: "Page " counter(page) " of " counter(pages);
          }
          }

          .header {
          position: running(header);
          }
        </style>
			</head>
			<body>
				<xsl:for-each select="/n1:ReceiptAdviceReceived/mdrcp:ReceiptAdvice">
				<xsl:variable name="ReceiptTypeVar">
					<xsl:choose>
						<xsl:when test="cac:Shipment/cac:Consignment/cbc:SummaryDescription=&apos;Service&apos;">
							<xsl:value-of select="&apos;SVC&apos;" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="&apos;GDS&apos;" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>

				<xsl:variable name="ReceiptTitleDescVar">
					<xsl:choose>
						<xsl:when test="$ReceiptTypeVar=&apos;SVC&apos;">
							<xsl:value-of select="'Service Receipt'"/>
						</xsl:when>
						<xsl:when test="$ReceiptTypeVar=&apos;GDS&apos;">
							<xsl:value-of select="'Goods Receipt'"/>
						</xsl:when>
					</xsl:choose>
				</xsl:variable>
				
					
				<xsl:variable name="PriceMixedMode">
					<xsl:choose>
						<xsl:when test="$ReceiptTypeVar=&apos;GDS&apos;">
							<xsl:value-of select="&apos;QTY&apos;" />
						</xsl:when>
						<xsl:when test="($ReceiptTypeVar=&apos;SVC&apos;)">
							<xsl:variable name="countAmt">
								<xsl:value-of select="count(cac:ReceiptLine[cac:Item/cac:AdditionalItemProperty[(cbc:Name=&apos;Type&apos;) and (cbc:Value=&apos;AMT&apos;)]])"/>
							</xsl:variable>
							<xsl:variable name="countTotal">
								<xsl:value-of select="count(cac:ReceiptLine)"/>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="$countAmt = $countTotal">
									<xsl:value-of select="&apos;AMT&apos;" />
								</xsl:when>
								<xsl:when test="$countAmt = 0">
									<xsl:value-of select="&apos;QTY&apos;" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="&apos;MIX&apos;" />
								</xsl:otherwise>
							</xsl:choose>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="&apos;QTY&apos;" />
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				
				<div class="header">
					<table align="center" width="100%" cellpadding="1" cellspacing="0" border="1" class="noborder">
						<tbody>
						<tr><td class="cellNoBorder" colspan="3"><xsl:text>&#160;</xsl:text></td></tr>
							<tr>
								<th class="cellNoBorder" width="33.33%" align="left">
									<xsl:for-each select="tokenize(cac:DeliveryCustomerParty/cac:Party/cac:PartyName/cbc:Name,'#')">
										<xsl:value-of select="."/><br/>
									</xsl:for-each>
								</th>
								<td class="cellNoBorder" width="33.33%" align="center"><b><xsl:value-of select="$ReceiptTitleDescVar"/></b><br/><xsl:value-of select="cbc:ID"/></td>
								<td class="cellNoBorder" width="33.33%" align="right"><b>Rendering date</b><br/><xsl:value-of select="format-dateTime(current-dateTime(), '[D01]/[M01]/[Y0001] at [H01]:[m01]')"/></td>
							</tr>
							<tr><td class="cellNoBorder" colspan="3"><xsl:text>&#160;</xsl:text></td></tr>
						</tbody>
					</table>
					
					<table align="center" width="100%" cellpadding="3" cellspacing="0" border="1" class="border">
						<tbody>
							<tr>
								<th class="cellNoBorder" width="11%" align="right">Reception Date:</th>
								<td class="cellNoBorder" width="19%">
									<xsl:value-of select="substring(string((cac:ReceiptLine/cbc:ReceivedDate)[1]), 9, 2)"/>
									<xsl:text>/</xsl:text>
									<xsl:value-of select="substring(string((cac:ReceiptLine/cbc:ReceivedDate)[1]), 6, 2)"/>
									<xsl:text>/</xsl:text>
									<xsl:value-of select="substring(string((cac:ReceiptLine/cbc:ReceivedDate)[1]), 1, 4)"/>
								</td>
								<th class="cellNoBorder" width="11%" align="right">Responsible Center:</th>
								<td class="cellNoBorder" width="15%"><xsl:value-of select="cac:BuyerCustomerParty/cac:Party/cac:PartyName/cbc:Name"/></td>
								<th class="cellNoBorder" width="10%" align="right">Supplier:</th>
								<td class="cellNoBorder" width="24%"><xsl:value-of select="cac:DespatchSupplierParty/cac:Party/cac:PartyIdentification/cbc:ID"/> - <xsl:value-of select="cac:DespatchSupplierParty/cac:Party/cac:PartyName/cbc:Name"/></td>
							</tr>
							<tr>
								<th class="cellNoBorder" align="right">Order/Internal ref.:</th>
								<td class="cellNoBorder">
									<xsl:value-of select="cac:OrderReference/cbc:ID"/> / 
									<xsl:for-each select="cac:AdditionalDocumentReference">
										<xsl:if test="cbc:DocumentType=&apos;Order Internal Reference&apos;">
											<xsl:value-of select="cbc:ID"/>
										</xsl:if>
									</xsl:for-each>
								</td>
								<th class="cellNoBorder" align="right">Assignment:</th>
								<td class="cellNoBorder"><xsl:value-of select="cac:Shipment/cac:Consignment/cac:FinalDeliveryParty/cac:PartyName/cbc:Name"/></td>
								<th class="cellNoBorder" align="right">
									<xsl:for-each select="cac:AdditionalDocumentReference">
										<xsl:if test="cbc:DocumentType=&apos;Procedure&apos;">Procedure:</xsl:if>
									</xsl:for-each>
								</th>
								<td class="cellNoBorder">
									<xsl:for-each select="cac:AdditionalDocumentReference">
										<xsl:if test="(cbc:DocumentType=&apos;Procedure&apos;)">
											<xsl:value-of select="cbc:ID"/>
										</xsl:if>
									</xsl:for-each>
								</td>
							</tr>
              
							<tr>
								<xsl:choose>
									<xsl:when test="$ReceiptTypeVar=&apos;SVC&apos;">
										<th class="cellNoBorder" align="right">Service Provision<br/>Start date:</th>
										<td class="cellNoBorder">
											<xsl:value-of select="substring(string((cac:ReceiptLine/cac:Item/cac:AdditionalItemProperty/cac:UsabilityPeriod/cbc:StartDate)[1]), 9, 2)"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="substring(string((cac:ReceiptLine/cac:Item/cac:AdditionalItemProperty/cac:UsabilityPeriod/cbc:StartDate)[1]), 6, 2)"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="substring(string((cac:ReceiptLine/cac:Item/cac:AdditionalItemProperty/cac:UsabilityPeriod/cbc:StartDate)[1]), 1, 4)"/>
										</td>
										<th class="cellNoBorder" align="right">Service Provision<br/>End  date:</th>
										<td class="cellNoBorder">
											<xsl:value-of select="substring(string((cac:ReceiptLine/cac:Item/cac:AdditionalItemProperty/cac:UsabilityPeriod/cbc:EndDate)[1]), 9, 2)"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="substring(string((cac:ReceiptLine/cac:Item/cac:AdditionalItemProperty/cac:UsabilityPeriod/cbc:EndDate)[1]), 6, 2)"/>
											<xsl:text>/</xsl:text>
											<xsl:value-of select="substring(string((cac:ReceiptLine/cac:Item/cac:AdditionalItemProperty/cac:UsabilityPeriod/cbc:EndDate)[1]), 1, 4)"/>
										</td>
									</xsl:when>
									<xsl:when test="$ReceiptTypeVar=&apos;GDS&apos;">
										<th class="cellNoBorder" align="right">Work:</th>
										<td class="cellNoBorder"><xsl:text>&#160;</xsl:text></td>
										<th class="cellNoBorder" align="right">Stock Ref:</th>
										<td class="cellNoBorder"><xsl:text>&#160;</xsl:text></td>
									</xsl:when>
								</xsl:choose>

                <th class="cellNoBorder" align="right">
                  <xsl:for-each select="cac:AdditionalDocumentReference">
                    <xsl:if test="cbc:DocumentTypeCode=&apos;149&apos;">Contract No (Framework/Direct):</xsl:if>
                    <xsl:if test="cbc:DocumentTypeCode=&apos;220&apos;">Contract No (Framework/Direct):</xsl:if>
                  </xsl:for-each>
                </th>
                <td class="cellNoBorder">
                  <xsl:for-each select="cac:AdditionalDocumentReference">
                    <xsl:if test="(cbc:DocumentTypeCode=&apos;149&apos;) or (cbc:DocumentTypeCode=&apos;220&apos;)">
                      <xsl:value-of select="cbc:ID"/>
                    </xsl:if>
                  </xsl:for-each>
                </td>
							</tr>
              
              <tr>
                <th class="cellNoBorder" align="right">
                  Dispatch Advice ref:
                </th>
                <td class="cellNoBorder">
                  <xsl:choose>
                    <xsl:when test="cac:DespatchDocumentReference/cbc:ID!=&apos;&apos;">
                      <xsl:value-of select="cac:DespatchDocumentReference/cbc:ID"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:text>N/A</xsl:text>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
                <th class="cellNoBorder" align="right">
                  Dispatch Advice Date:
                </th>
                <td class="cellNoBorder">
                  <xsl:choose>
                    <xsl:when test="cac:DespatchDocumentReference/cbc:IssueDate!=&apos;&apos;">
                      <xsl:value-of select="substring(string(cac:DespatchDocumentReference/cbc:IssueDate), 9, 2)"/>
                      <xsl:text>/</xsl:text>
                      <xsl:value-of select="substring(string(cac:DespatchDocumentReference/cbc:IssueDate), 6, 2)"/>
                      <xsl:text>/</xsl:text>
                      <xsl:value-of select="substring(string(cac:DespatchDocumentReference/cbc:IssueDate), 1, 4)"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:text>N/A</xsl:text>
                    </xsl:otherwise>
                  </xsl:choose>
                </td>
                <th class="cellNoBorder" align="right">
                  <xsl:for-each select="cac:AdditionalDocumentReference">
                    <xsl:if test="cbc:DocumentType=&apos;Specific Contract&apos;">
                      PO/SC/OF:
                    </xsl:if>

                    <xsl:if test="cbc:DocumentType=&apos;Purchase Order&apos;">
                      PO/SC/OF:
                    </xsl:if>
                  </xsl:for-each>
                </th>
                <td class="cellNoBorder">
                  <xsl:for-each select="cac:AdditionalDocumentReference">
                    <xsl:if test="cbc:DocumentType=&apos;Specific Contract&apos;">
                      <xsl:value-of select="cbc:ID"/>
                    </xsl:if>
                    <xsl:if test="cbc:DocumentType=&apos;Purchase Order&apos;">
                      <xsl:value-of select="cbc:ID"/>
                    </xsl:if>
                  </xsl:for-each>
                </td>
              </tr>
              
              <!--
                              <th class="cellNoBorder" align="right">
                  <xsl:text>Financial Rules:</xsl:text>
                </th>
                <td class="cellNoBorder">
                  <xsl:for-each select="ext:UBLExtensions">
                    <xsl:for-each select="ext:UBLExtension">
                      <xsl:if test="cbc:ID=2">
                        <xsl:value-of select="ext:ExtensionContent/receiptExt:FinancialRules/receiptExt:FinancialRegulationsGroupCode"/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:for-each>
                </td>
              
              <tr>
                <th class="cellNoBorder" align="right">
                  <xsl:text>&#160;</xsl:text>
                </th>
                <td class="cellNoBorder">
                  <xsl:text>&#160;</xsl:text>
                </td>
                <th class="cellNoBorder" align="right">
                  <xsl:text>&#160;</xsl:text>
                </th>
                <td class="cellNoBorder">
                  <xsl:text>&#160;</xsl:text>
                </td>
                <th class="cellNoBorder" align="right">
                  <xsl:text>Bank Account:</xsl:text>
                </th>
                <td class="cellNoBorder">
                  <xsl:for-each select="ext:UBLExtensions">
                    <xsl:for-each select="ext:UBLExtension">
                      <xsl:if test="cbc:ID=1">
                        <xsl:value-of select="ext:ExtensionContent/receiptExt:IBANBankAccount"/>
                      </xsl:if>
                    </xsl:for-each>
                  </xsl:for-each>
                </td>
              </tr>
-->
						</tbody>
					</table>
					</div>

					<table align="center" width="100%" cellpadding="3" cellspacing="0" border="1" >
						<thead>
							<tr><td class="cellNoBorder" colspan="12"><xsl:text>&#160;</xsl:text></td></tr>
							<tr>
								<xsl:choose>
									<xsl:when test="$ReceiptTypeVar=&apos;GDS&apos;">
										<th class="cellNoBorder" width="8%">Identifier</th>
										<th class="cellNoBorder" width="4%">Order seq</th>
										<th class="cellNoBorder" width="4%">GR Seq</th>
										<th class="cellNoBorder" width="28%">Description</th>
										<th class="cellNoBorder" width="4%">Main Asset</th>
                    <th class="cellNoBorder" width="7%">Ordered</th>
										<th class="cellNoBorder" width="7%">Received Quantity</th>
										<th class="cellNoBorder" width="8%">Cumulated qty in GR seq</th>
										<th class="cellNoBorder" width="9%">Asset Class</th>
										<th class="cellNoBorder" width="8%">Unit Price</th>

                    <!-- Not present for Goods
                    <th class="cellNoBorder" width="4%">ToDeadline</th>
                    <th class="cellNoBorder" width="4%">Evaluation Mark</th>
                    <th class="cellNoBorder" width="4%">Received Date</th>
                    -->
                    
										<th class="cellNoBorder" width="9%">Request</th>
										<th class="cellNoBorder" width="4%">Sticker</th>
									</xsl:when>
									<!-- Labels for SVC depending on MUX/AMT/QTY    [MIX] -->
									<xsl:when test="$ReceiptTypeVar=&apos;SVC&apos; and $PriceMixedMode=&apos;MIX&apos;">
										<th class="cellNoBorder" width="1%"></th>
										<th class="cellNoBorder" align="center" width="8%">Order seq</th>
										<th class="cellNoBorder" align="center" width="10%">Receipt Seq</th>
										<th class="cellNoBorder" width="16%">Description</th>
										<!-- missing Main Asset -->
                    <th class="cellNoBorder" align="center" width="11%">
                      Ordered <br/>qty/amount
                    </th>
										<th class="cellNoBorder" align="center" width="11%">Received <br/>qty/amount</th>
										<th class="cellNoBorder" align="center" width="11%">Total received <br/>amount</th>
										<th class="cellNoBorder" align="center" width="11%">Cumulated received<br/>qty/amount</th>
										<th class="cellNoBorder" align="center" width="11%">Remaining <br/>qty/amount</th>
										<!-- missing Asset Class -->
										<th class="cellNoBorder" width="8%">Unit Price</th>

                    <!-- eTrefle special -->
                    <th class="cellNoBorder" width="4%">ToDeadline</th>
                    <th class="cellNoBorder" width="4%">Evaluation Mark</th>
                    <th class="cellNoBorder" width="4%">Received Date</th>
                    
										<th class="cellNoBorder" width="1%"></th>
										<!-- missing Sticker -->
									</xsl:when>
									<!-- Labels for SVC depending on MUX/AMT/QTY    [AMT] -->
									<xsl:when test="$ReceiptTypeVar=&apos;SVC&apos; and $PriceMixedMode=&apos;AMT&apos;">
										<th class="cellNoBorder" width="1%"></th>
										<th class="cellNoBorder" align="center" width="8%">Order seq</th>
										<th class="cellNoBorder" align="center" width="10%">Receipt Seq</th>
										<th class="cellNoBorder" width="16%">Description</th>
										<!-- missing Main Asset -->
                    <th class="cellNoBorder" align="center" width="11%">
                      Ordered <br/>amount
                    </th>
										<th class="cellNoBorder" align="center" width="11%">Received <br/>amount</th>
										<th class="cellNoBorder" align="center" width="11%">Total received <br/>amount</th>
										<th class="cellNoBorder" align="center" width="11%">Cumulated received<br/>amount</th>
										<th class="cellNoBorder" align="center" width="11%">Remaining <br/>amount</th>
										<!-- missing Asset Class -->
										<th class="cellNoBorder" width="8%"><xsl:text>&#160;</xsl:text></th>

                    <!-- eTrefle special -->
                    <th class="cellNoBorder" width="4%">ToDeadline</th>
                    <th class="cellNoBorder" width="4%">Evaluation Mark</th>
                    <th class="cellNoBorder" width="4%">Received Date</th>
                    
										<th class="cellNoBorder" width="1%"></th>
										<!-- missing Sticker -->
									</xsl:when>
									<!-- Labels for SVC depending on MUX/AMT/QTY    [QTY] -->
									<xsl:when test="$ReceiptTypeVar=&apos;SVC&apos; and $PriceMixedMode=&apos;QTY&apos;">
										<th class="cellNoBorder" width="1%"></th>
										<th class="cellNoBorder" align="center" width="8%">Order seq</th>
										<th class="cellNoBorder" align="center" width="10%">Receipt Seq</th>
										<th class="cellNoBorder" width="16%">Description</th>
										<!-- missing Main Asset -->
                    <th class="cellNoBorder" align="center" width="11%">
                      Ordered <br/>quantity
                    </th>
										<th class="cellNoBorder" align="center" width="11%">Received <br/>quantity</th>
										<th class="cellNoBorder" align="center" width="11%">Total received <br/>amount</th>
										<th class="cellNoBorder" align="center" width="11%">Cumulated<br/>received quantity</th>
										<th class="cellNoBorder" align="center" width="11%">Remaining <br/>quantity</th>
										<!-- missing Asset Class -->
										<th class="cellNoBorder" width="8%">Unit Price</th>

                    <!-- eTrefle special -->
                    <th class="cellNoBorder" width="4%">ToDeadline</th>
                    <th class="cellNoBorder" width="4%">Evaluation Mark</th>
                    <th class="cellNoBorder" width="4%">Received Date</th>
                    
										<th class="cellNoBorder" width="1%"></th>
										<!-- missing Sticker -->
									</xsl:when>
								</xsl:choose>
							</tr>
							<tr>
								<xsl:choose>
									<xsl:when test="$ReceiptTypeVar=&apos;GDS&apos;">
										<th class="cellNoBorder" width="8%"></th>
										<th class="cellNoBorder" width="4%"></th>
										<th class="cellNoBorder" width="4%"></th>
										<th class="cellNoBorder" width="28%"></th>
										<th class="cellNoBorder" width="4%"></th>
                    <th class="cellNoBorder" width="7%"><!--Ordered--></th>
										<th class="cellNoBorder" width="7%"><!--Received--></th>
										<th class="cellNoBorder" width="8%"></th>
										<th class="cellNoBorder" width="9%"></th>
										<th class="cellNoBorder" width="8%"></th>
										<th class="cellNoBorder" width="9%"></th>
										<th class="cellNoBorder" width="4%"></th>
									</xsl:when>
									<xsl:when test="$ReceiptTypeVar=&apos;SVC&apos;">
										<th class="cellNoBorder" width="1%"></th>
										<th class="cellNoBorder" width="8%"></th>
										<th class="cellNoBorder" width="10%"></th>
										<th class="cellNoBorder" width="16%"></th>
										<!-- missing Main Asset -->
                    <th class="cellNoBorder" width="11%"><!--Ordered--></th>
										<th class="cellNoBorder" width="11%"><!--Received--></th>
										<th class="cellNoBorder" width="11%"></th>
										<th class="cellNoBorder" width="11%"></th>
										<th class="cellNoBorder" width="11%"></th>
										<!-- missing Asset Class -->
										<th class="cellNoBorder" width="8%"></th>

                    <!-- eTrefle special -->
                    <th class="cellNoBorder" width="4%"><!--ToDeadline--></th>
                    <th class="cellNoBorder" width="4%"><!--Evaluation Mark--></th>
                    <th class="cellNoBorder" width="4%"><!--Received Date--></th>
                    
										<th class="cellNoBorder" width="1%"></th>
										<!-- missing Sticker -->
									</xsl:when>
								</xsl:choose>
							</tr>
						</thead>
						<tbody>
							<xsl:for-each select="cac:ReceiptLine">
									<xsl:variable name="amountFlag">
									<xsl:for-each select="cac:Item/cac:AdditionalItemProperty">
										<xsl:if test="(cbc:Name=&apos;Type&apos;) and (cbc:Value=&apos;AMT&apos;) and ($ReceiptTypeVar=&apos;SVC&apos;)">
											<xsl:value-of select="&apos;AMT&apos;" />
										</xsl:if>
									</xsl:for-each>
 									</xsl:variable>

                <xsl:variable name="penaltyFlag">
                  <xsl:choose>
                    <xsl:when test="cac:Shipment/cac:GoodsItem/cbc:ValueAmount &lt; 0">
                      <xsl:value-of select="&apos;true&apos;" />
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:value-of select="&apos;false&apos;" />
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:variable>
								<tr>
									<td class="cellTopBorder" align="center">
									<xsl:if test="$ReceiptTypeVar=&apos;GDS&apos;">
										<xsl:for-each select="cac:Item/cac:AdditionalItemProperty">
											<xsl:if test="cbc:Name=&apos;Asset Flag&apos;">
												<xsl:if test="cbc:Value=&apos;true&apos;">Asset</xsl:if>
												<xsl:if test="cbc:Value!=&apos;true&apos;"><xsl:text>&#160;</xsl:text></xsl:if>
											</xsl:if>
										</xsl:for-each>
									</xsl:if>
									</td>
									<td class="cellTopBorder" align="center"><xsl:value-of select="cac:OrderLineReference/cbc:LineID"/></td>
									<td class="cellTopBorder" align="center"><xsl:value-of select="cbc:ID"/></td>
									<td class="cellTopBorder"><xsl:value-of select="cac:Item/cbc:Name"/></td>
									<xsl:if test="$ReceiptTypeVar=&apos;GDS&apos;">
										<td class="cellTopBorder" align="center">
											<xsl:for-each select="cac:Item/cac:AdditionalItemProperty">
												<xsl:if test="cbc:Name=&apos;Main Asset&apos;">
													<xsl:value-of select="cbc:Value"/>
												</xsl:if>
											</xsl:for-each>
											<xsl:text>&#160;</xsl:text>
										</td>
									</xsl:if>

                  <td class="cellTopBorder" align="center">
                    <xsl:if test="$penaltyFlag=&apos;false&apos;">
                    <xsl:value-of select="cac:Shipment/cbc:Information"/>
                    <!-- Ordered Qty/Amount -->
                    <xsl:if test="$amountFlag=&apos;AMT&apos;">
                      <xsl:text> </xsl:text>
                      <xsl:value-of select="cac:Shipment/cac:GoodsItem/cbc:ValueAmount/@currencyID"/>
                    </xsl:if>
                    <xsl:if test="$amountFlag=&apos;QTY&apos;">
                      <xsl:text> </xsl:text>
                      <xsl:value-of select="cbc:ReceivedQuantity/@unitCode"/>
                    </xsl:if>
                    </xsl:if>
                  </td>
                  
									<td class="cellTopBorder" align="center">
                    <!--Received-->
										<xsl:choose>
											<xsl:when test="$amountFlag=&apos;AMT&apos; and $penaltyFlag=&apos;false&apos;">
												<xsl:for-each select="cac:Item/cac:AdditionalItemProperty">
													<xsl:if test="cbc:Name=&apos;Unit Price&apos;">
														<xsl:value-of select="format-number(number(cbc:Value), '##0,00', 'format1')"/><xsl:text> </xsl:text>
													</xsl:if>
												</xsl:for-each>
												<xsl:value-of select="cac:Shipment/cac:GoodsItem/cbc:ValueAmount/@currencyID"/>
											</xsl:when>
                      <xsl:when test="$amountFlag=&apos;AMT&apos; and $penaltyFlag=&apos;true&apos;">
                        <!-- For penalty shows the ValueAmount instead of unitPrice for Amount based-->
                        <xsl:for-each select="cac:Shipment/cac:GoodsItem">
                          <xsl:value-of select="format-number(number(cbc:ValueAmount), '##0,00', 'format1')"/>
                          <xsl:text> </xsl:text>
                        </xsl:for-each>
                        <xsl:value-of select="cac:Shipment/cac:GoodsItem/cbc:ValueAmount/@currencyID"/>
                      </xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="format-number(number(cbc:ReceivedQuantity), '###0,00', 'format1')"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="cbc:ReceivedQuantity/@unitCode"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
									<xsl:if test="$ReceiptTypeVar=&apos;SVC&apos;">
									<td class="cellTopBorder" align="center">
											<xsl:for-each select="cac:Shipment/cac:GoodsItem">
													<xsl:value-of select="format-number(number(cbc:ValueAmount), '##0,00', 'format1')"/><xsl:text> </xsl:text>
											</xsl:for-each>
											<xsl:value-of select="cac:Shipment/cac:GoodsItem/cbc:ValueAmount/@currencyID"/>
									</td>
									</xsl:if>
									<td class="cellTopBorder" align="center">
                    <xsl:if test="$penaltyFlag=&apos;false&apos;">
                      <xsl:if test="$ReceiptTypeVar=&apos;GDS&apos;">
                        <xsl:value-of select="cac:Shipment/cac:Consignment/cbc:SummaryDescription"/>
                      </xsl:if>
                      <xsl:if test="$ReceiptTypeVar=&apos;SVC&apos;">
                        <xsl:value-of select="format-number(number(cac:Shipment/cac:Consignment/cbc:SummaryDescription), '##0,00', 'format1')"/>
                        <xsl:text> </xsl:text>
                      </xsl:if>
                      <xsl:if test="$amountFlag=&apos;AMT&apos;">
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="cac:Shipment/cac:GoodsItem/cbc:ValueAmount/@currencyID"/>
                      </xsl:if>
                      <xsl:if test="$amountFlag=&apos;QTY&apos;">
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="cbc:ReceivedQuantity/@unitCode"/>
                      </xsl:if>
                    </xsl:if>
									</td>
									<xsl:if test="$ReceiptTypeVar=&apos;SVC&apos;">
									<td class="cellTopBorder" align="center">
                    <xsl:if test="$penaltyFlag=&apos;false&apos;">
                      <xsl:for-each select="cac:Item/cac:AdditionalItemProperty">
                        <xsl:if test="cbc:Name=&apos;RemainingValue&apos;">
                          <xsl:value-of select="format-number(number(cbc:Value), '##0,00', 'format1')"/>
                          <xsl:text> </xsl:text>
                        </xsl:if>
                      </xsl:for-each>
                      <xsl:if test="$amountFlag=&apos;AMT&apos;">
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="cac:Shipment/cac:GoodsItem/cbc:ValueAmount/@currencyID"/>
                      </xsl:if>
                      <xsl:if test="$amountFlag=&apos;QTY&apos;">
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="cbc:ReceivedQuantity/@unitCode"/>
                      </xsl:if>
                    </xsl:if>
									</td>
									</xsl:if>

									<xsl:if test="$ReceiptTypeVar=&apos;GDS&apos;">
										<td class="cellTopBorder" align="center">
											<xsl:for-each select="cac:Item/cac:AdditionalItemProperty">
												<xsl:if test="cbc:Name=&apos;Asset Class&apos;">
													<xsl:value-of select="cbc:Value"/>
												</xsl:if>
											</xsl:for-each>
											<xsl:text>&#160;</xsl:text>
										</td>
									</xsl:if>
									<td class="cellTopBorder" align="center" nowrap="nowrap">
										<xsl:choose>
											<xsl:when test="$amountFlag=&apos;AMT&apos;">
												<xsl:choose>
													<xsl:when test="$PriceMixedMode=&apos;MIX&apos;">
														<xsl:text>N/A</xsl:text>
													</xsl:when>
													<xsl:otherwise>
														<!-- Print nothing on amount for the unit price -->
														<xsl:text>&#160;</xsl:text>
													</xsl:otherwise>
												</xsl:choose>
											</xsl:when>
											<xsl:otherwise>
												<xsl:for-each select="cac:Item/cac:AdditionalItemProperty">
													<xsl:if test="cbc:Name=&apos;Unit Price&apos;">
														<xsl:value-of select="format-number(number(cbc:Value), '##0,00', 'format1')"/><xsl:text> </xsl:text>
													</xsl:if>
												</xsl:for-each>
												<xsl:value-of select="cac:Shipment/cac:GoodsItem/cbc:ValueAmount/@currencyID"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
                  <xsl:if test="$ReceiptTypeVar=&apos;SVC&apos;">
                    <xsl:variable name="currentId" select="cbc:ID"/>
                    
                    <td class="cellTopBorder" align="center">
                      <!--ReceiptAdvice/UBLExtensions/UBLExtension/ReceiptLine/ToDeadline-->
                      <xsl:if test="$penaltyFlag=&apos;false&apos;">
                        <xsl:for-each select="/n1:ReceiptAdviceReceived/mdrcp:ReceiptAdvice">
                          <xsl:for-each select="ext:UBLExtensions">
                            <xsl:for-each select="ext:UBLExtension">
                              <xsl:if test="cbc:ID=3">
                                <xsl:for-each select="ext:ExtensionContent/receiptExt:ReceiptLines">
                                  <xsl:for-each select="receiptExt:ReceiptLine">
                                    <xsl:if test="receiptExt:ID=$currentId">
                                      <xsl:value-of select="receiptExt:MarkToDeadLine"/>
                                    </xsl:if>
                                  </xsl:for-each>
                                </xsl:for-each>
                              </xsl:if>
                            </xsl:for-each>
                          </xsl:for-each>
                        </xsl:for-each>
                      </xsl:if>
                    </td>
                    <td class="cellTopBorder" align="center">
                      <!--ReceiptAdvice/UBLExtensions/UBLExtension/ReceiptLine/Evaluation Mark-->
                      <xsl:if test="$penaltyFlag=&apos;false&apos;">
                        <xsl:for-each select="/n1:ReceiptAdviceReceived/mdrcp:ReceiptAdvice">
                          <xsl:for-each select="ext:UBLExtensions">
                            <xsl:for-each select="ext:UBLExtension">
                              <xsl:if test="cbc:ID=3">
                                <xsl:for-each select="ext:ExtensionContent/receiptExt:ReceiptLines">
                                  <xsl:for-each select="receiptExt:ReceiptLine">
                                    <xsl:if test="receiptExt:ID=$currentId">
                                      <xsl:value-of select="receiptExt:EvaluationMark"/>
                                    </xsl:if>
                                  </xsl:for-each>
                                </xsl:for-each>
                              </xsl:if>
                            </xsl:for-each>
                          </xsl:for-each>
                        </xsl:for-each>
                      </xsl:if>
                    </td>
                    <td class="cellTopBorder" align="center">
                      <!--ReceiptAdvice/UBLExtensions/UBLExtension/ReceiptLine/ReceivedDate-->
                      <xsl:if test="$penaltyFlag=&apos;false&apos;">
                        <xsl:for-each select="/n1:ReceiptAdviceReceived/mdrcp:ReceiptAdvice">
                          <xsl:for-each select="ext:UBLExtensions">
                            <xsl:for-each select="ext:UBLExtension">
                              <xsl:if test="cbc:ID=3">
                                <xsl:for-each select="ext:ExtensionContent/receiptExt:ReceiptLines">
                                  <xsl:for-each select="receiptExt:ReceiptLine">
                                    <xsl:if test="receiptExt:ID=$currentId">
                                      <xsl:value-of select="substring(string(receiptExt:ReceivedDate), 9, 2)"/>
                                      <xsl:text>/</xsl:text>
                                      <xsl:value-of select="substring(string(receiptExt:ReceivedDate), 6, 2)"/>
                                      <xsl:text>/</xsl:text>
                                      <xsl:value-of select="substring(string(receiptExt:ReceivedDate), 1, 4)"/>
                                    </xsl:if>
                                  </xsl:for-each>
                                </xsl:for-each>
                              </xsl:if>
                            </xsl:for-each>
                          </xsl:for-each>
                        </xsl:for-each>
                      </xsl:if>
                    </td>
                  </xsl:if>
									<td class="cellTopBorder" align="center">
										<xsl:if test="cac:DocumentReference/cbc:DocumentType=&apos;Request&apos;">
											<xsl:value-of select="cac:DocumentReference/cbc:ID"/>
										</xsl:if>
										<xsl:text>&#160;</xsl:text>
									</td>
									<xsl:if test="$ReceiptTypeVar=&apos;GDS&apos;">
										<td class="cellTopBorder" align="center"><xsl:text>&#160;</xsl:text></td>
									</xsl:if>
								</tr>
								<tr><td class="cellNoBorder" colspan="12"><xsl:text>&#160;</xsl:text></td></tr>
								
								
								<xsl:choose>
									<xsl:when test="$ReceiptTypeVar=&apos;GDS&apos;">
								<tr>
									<td class="cellNoBorder" colspan="7" align="left">
										<table width="100%" cellpadding="1" cellspacing="0" border="0">
											<tbody>
												<xsl:for-each select="cac:Item/cac:ItemInstance">
												<tr>
													<td width="40%"><xsl:value-of select="cbc:RegistrationID"/></td>
													<td width="30%"><b>Key: </b><xsl:value-of select="cbc:ProductTraceID"/></td>
													<td width="30%"><b>Sn: </b><xsl:value-of select="cbc:SerialID"/></td>
												</tr>
												</xsl:for-each>
											</tbody>
										</table>
									</td>
									<td class="cellNoBorder" colspan="3"><xsl:value-of select="a"/>
										<xsl:if test="cac:Item/cbc:Keyword!=&apos;&apos;">
											<xsl:value-of select="cac:Item/cbc:Keyword"/> - <xsl:value-of select="cac:Item/cac:BuyersItemIdentification/cbc:ID"/> - <xsl:value-of select="cac:Item/cbc:Description"/>
										</xsl:if>
									</td>
									<td class="cellNoBorder" colspan="2">
										<xsl:for-each select="cac:Item/cac:AdditionalItemProperty">
											<xsl:if test="cbc:Name=&apos;Installation Date&apos;">
												<b>Installation Date: </b>
												<xsl:value-of select="cbc:Value"/>
											</xsl:if>
										</xsl:for-each>
									</td>
								</tr>
									</xsl:when>
									<xsl:when test="$ReceiptTypeVar=&apos;SVC&apos;">
									<tr>
										<td class="cellNoBorder" colspan="6" align="left">
											<table width="100%" cellpadding="1" cellspacing="0" border="0">
												<tbody>
													<xsl:for-each select="cac:Item/cac:ItemInstance">
													<tr>
														<td width="40%"><xsl:value-of select="cbc:RegistrationID"/></td>
														<td width="30%"><b>Key: </b><xsl:value-of select="cbc:ProductTraceID"/></td>
														<td width="30%"><b>Sn: </b><xsl:value-of select="cbc:SerialID"/></td>
													</tr>
													</xsl:for-each>
												</tbody>
											</table>
										</td>
										<td class="cellNoBorder" colspan="2"><xsl:value-of select="a"/>
											<xsl:if test="cac:Item/cbc:Keyword!=&apos;&apos;">
												<xsl:value-of select="cac:Item/cbc:Keyword"/> - <xsl:value-of select="cac:Item/cac:BuyersItemIdentification/cbc:ID"/> - <xsl:value-of select="cac:Item/cbc:Description"/>
											</xsl:if>
										</td>
										<td class="cellNoBorder" colspan="1">
											<xsl:for-each select="cac:Item/cac:AdditionalItemProperty">
												<xsl:if test="cbc:Name=&apos;Installation Date&apos;">
													<b>Installation Date: </b>
													<xsl:value-of select="cbc:Value"/>
												</xsl:if>
											</xsl:for-each>
										</td>
									</tr>
									</xsl:when>
								</xsl:choose>
								
								<tr><td class="cellNoBorder" colspan="12"><xsl:text>&#160;</xsl:text></td></tr>
							</xsl:for-each>
						</tbody>
					</table>
					
					<table align="center" width="100%" cellpadding="3" cellspacing="0" border="1" class="border">
						<tbody>
							<xsl:variable name="Signatures" select="count(cac:Signature)"/>
							<tr>
								<xsl:choose>
									<xsl:when test="$ReceiptTypeVar=&apos;GDS&apos;">
										<td class="cellNoBorder" width="13%"><b>Destination:</b></td>
										<td class="cellNoBorder" width="27%"><xsl:value-of select="cac:Shipment/cac:Delivery/cac:DeliveryLocation/cbc:Description"/></td>
										<td class="cellLeftRightBorder" width="20%" rowspan="5" align="center" style="vertical-align:text-top;"><b>For reception:</b><br/><br/>
											<xsl:for-each select="(cac:Signature/cac:SignatoryParty)[$Signatures]">
												<xsl:value-of select="cac:Person/cbc:Title"/><br/>
												<xsl:value-of select="cac:Person/cbc:FamilyName"/><xsl:text> </xsl:text>
												<xsl:value-of select="cac:Person/cbc:FirstName"/>
												<br/><br/>
												<xsl:value-of select="cac:PartyName/cbc:Name"/>
											</xsl:for-each>
										</td>
									</xsl:when>
									<xsl:when test="$ReceiptTypeVar=&apos;SVC&apos;">
										<td class="cellNoBorder" width="13%"><b>Receiving service remarks:</b></td>
										<td class="cellNoBorder" width="27%" rowspan="2"><xsl:value-of select="(cbc:Note)[2]"/><xsl:text>&#160;</xsl:text></td>
										<td class="cellLeftRightBorder" width="20%" rowspan="5" align="center" style="vertical-align:text-top;">
                      <b>Service Entry Agent:</b>
                      <br/><br/>
                      <xsl:if test="cac:DespatchDocumentReference/cbc:ID=&apos;&apos;">
                        <xsl:value-of select="cac:SellerSupplierParty/cac:Party/cac:Partyname/cbc:Name"/>
                      </xsl:if>

                      <!--
                      <xsl:for-each select="(cac:Signature/cac:SignatoryParty)[1]"><br/>
												<xsl:value-of select="cac:Person/cbc:Title"/><br/>
												<xsl:value-of select="cac:Person/cbc:FamilyName"/><xsl:text> </xsl:text>
												<xsl:value-of select="cac:Person/cbc:FirstName"/>
											</xsl:for-each>
                      -->
										</td>
									</xsl:when>
								</xsl:choose>
								<td class="cellNoBorder" width="40%" rowspan="2" style="vertical-align:text-top;"><b>PO/SC delivery: </b><xsl:value-of select="cbc:DocumentStatusCode"/></td>
							</tr>
							<tr>
							<xsl:choose>
								<xsl:when test="$ReceiptTypeVar=&apos;GDS&apos;">
									<td class="cellNoBorder"><b>Receiving service remarks:</b></td>
									<td class="cellNoBorder"><xsl:value-of select="(cbc:Note)[2]"/><xsl:text>&#160;</xsl:text></td>
								</xsl:when>
								<xsl:when test="$ReceiptTypeVar=&apos;SVC&apos;">
									<td class="cellNoBorder"><xsl:text>&#160;</xsl:text></td>
								</xsl:when>
							</xsl:choose>
							</tr>
							<tr>
								<td class="cellTopBorder" colspan="2"><b>Visas:</b><br/>
									<table width="99.99%" cellpadding="0" cellspacing="0" border="0">
										<tbody>
											<xsl:for-each select="cac:Signature/cac:SignatoryParty">
                      <tr>
													<xsl:choose>
													<xsl:when test="$ReceiptTypeVar=&apos;SVC&apos;">
														<xsl:if test="cac:Person/cbc:JobTitle=&apos;AOI&apos; or cac:Person/cbc:JobTitle=&apos;PRN&apos;">
															<td width="32%">
															<xsl:choose>
																<xsl:when test="cac:Person/cbc:JobTitle=&apos;AOI&apos;"><xsl:text>Operational Initiator Approbation</xsl:text></xsl:when>
																<xsl:when test="cac:Person/cbc:JobTitle=&apos;PRN&apos;"><xsl:text>Final validation</xsl:text></xsl:when>
																<xsl:otherwise>
																<xsl:value-of select="cac:Person/cbc:JobTitle"/>
																</xsl:otherwise>
															</xsl:choose>
															</td>
															<td width="48%"><xsl:value-of select="cac:Person/cbc:Title"/><xsl:text>&#160;</xsl:text>
															<xsl:value-of select="cac:Person/cbc:FamilyName"/><xsl:text>&#160;</xsl:text>
															<xsl:value-of select="cac:Person/cbc:FirstName"/></td>
															<td width="20%">
																<xsl:value-of select="substring(string(../cbc:ValidationDate), 9, 2)"/>
																<xsl:text>/</xsl:text>
																<xsl:value-of select="substring(string(../cbc:ValidationDate), 6, 2)"/>
																<xsl:text>/</xsl:text>
																<xsl:value-of select="substring(string(../cbc:ValidationDate), 1, 4)"/>
															</td>
														</xsl:if>
													</xsl:when>
													<xsl:otherwise>
														<xsl:if test="($Signatures=1) or (position()!=$Signatures)">
															<td width="32%"><xsl:value-of select="cac:Person/cbc:JobTitle"/></td>
															<td width="48%"><xsl:value-of select="cac:Person/cbc:Title"/><xsl:text>&#160;</xsl:text>
															<xsl:value-of select="cac:Person/cbc:FamilyName"/><xsl:text>&#160;</xsl:text>
															<xsl:value-of select="cac:Person/cbc:FirstName"/></td>
															<td width="20%">
																<xsl:value-of select="substring(string(../cbc:ValidationDate), 9, 2)"/>
																<xsl:text>/</xsl:text>
																<xsl:value-of select="substring(string(../cbc:ValidationDate), 6, 2)"/>
																<xsl:text>/</xsl:text>
																<xsl:value-of select="substring(string(../cbc:ValidationDate), 1, 4)"/>
															</td>
														</xsl:if>
													</xsl:otherwise>
													</xsl:choose>
												</tr>
											</xsl:for-each>
										</tbody>
									</table>
								</td>
								<td class="cellTopBorder" style="vertical-align:text-top;" rowspan="3"><b>Issue Date and Time: </b>
									<xsl:value-of select="substring(string(cbc:IssueDate), 9, 2)"/>
									<xsl:text>/</xsl:text>
									<xsl:value-of select="substring(string(cbc:IssueDate), 6, 2)"/>
									<xsl:text>/</xsl:text>
									<xsl:value-of select="substring(string(cbc:IssueDate), 1, 4)"/>
								at <xsl:value-of select="format-time(cbc:IssueTime, '[H01]:[m01]')"/></td>
							</tr>
							<tr>
								<xsl:choose>
								<xsl:when test="$ReceiptTypeVar=&apos;SVC&apos;">
									<td class="cellNoBorder"><xsl:text>&#160;</xsl:text></td>
									<td class="cellNoBorder"><xsl:text>&#160;</xsl:text></td>
								</xsl:when>
								<xsl:otherwise>
									<td class="cellNoBorder"><b>Manager:</b></td>
									<td class="cellNoBorder"><xsl:value-of select="cac:DeliveryCustomerParty/cac:Party/cac:Contact/cbc:Name"/><xsl:text>&#160;</xsl:text></td>
								</xsl:otherwise>
								</xsl:choose>
							</tr>
							<tr>
								<td class="cellNoBorder"><b>Created by:</b></td>
								<!-- <td class="cellNoBorder"><xsl:value-of select="(cbc:Note)[1]"/></td> -->
                <td class="cellNoBorder">
                  <xsl:value-of select="cac:SellerSupplierParty/cac:Party/cac:Partyname/cbc:Name"/>
                  <xsl:text>&#160;</xsl:text>
                </td>
							</tr>
						</tbody>
					</table>
					
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>