<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.CraneSoftwrights.com/ns/xslstyle"
                xmlns:ccs="urn:x-Crane:ss:Crane-Constraints2Schematron"
                exclude-result-prefixes="xs ccs"
                version="1.0">

<xs:doc info="$Id: Crane-UBL-Metadata.xsl,v 1.4 2008/11/11 16:33:28 gkholman Exp $"
        filename="Crane-UBL-Metadata.xsl" internal-ns="ccs" global-ns="xs"
        vocabulary="DocBook">
  <xs:title>CVA support for UBL instance-level meta data</xs:title>
  <para>
     Support for an implementation of the OASIS Code List Value Validation 
     Methodology.
  </para>
  <para>
     This fragment determines the names of UBL meta data information items
     related to code list information items.
  </para>
</xs:doc>
<!--
 Copyright (C) - Crane Softwrights Ltd.
               - http://www.CraneSoftwrights.com/links/res-dev.htm
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 - Redistributions of source code must retain the above copyright notice,
   this list of conditions and the following disclaimer.
 - Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.
 - The name of the author may not be used to endorse or promote products
   derived from this software without specific prior written permission.
 
 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 Note: for your reference, the above is the "Modified BSD license", this text
     was obtained 2003-07-26 at http://www.xfree86.org/3.3.6/COPYRIGHT2.html#5
-->

<xs:template>
  <para>
    For a given information item, engage the checking of various metadata
    properties supported by the external code list implementation by calling
    <literal>check-list-property</literal> for every possible instance-level
    meta data value.
  </para>
<itemizedlist>
<listitem><para>
for an attribute matching <literal>@currencyID</literal>:</para>
<itemizedlist>
<listitem><para>
<literal>@currencyCodeListVersionID</literal> mapped to genericode <literal>&lt;Version></literal> using the property <literal>Version</literal></para>
</listitem>
</itemizedlist>
</listitem>
<listitem><para>
for an attribute matching <literal>MeasureType/@unitCode</literal>:</para>
<itemizedlist>
<listitem><para>
<literal>@unitCodeListVersionID</literal> mapped to genericode <literal>&lt;Version></literal> using the property <literal>Version</literal></para>
</listitem>
</itemizedlist>
</listitem>
<listitem><para>
for an attribute matching <literal>QuantityType/@unitCode</literal>:</para>
<itemizedlist>
<listitem><para>
<literal>@unitCodeListID</literal> mapped to genericode <literal>&lt;Version></literal> using the property <literal>ID</literal></para></listitem>
<listitem><para><literal>@unitCodeListAgencyID</literal> mapped to genericode <literal>&lt;Agency>&lt;Identifier></literal> using the property <literal>AgencyID</literal></para>
</listitem>
<listitem><para><literal>@unitCodeListAgencyName</literal> mapped to genericode <literal>&lt;Agency>&lt;LongName></literal> using the property <literal>AgencyLongName</literal></para>
</listitem>
</itemizedlist>
</listitem>
<listitem><para>
for an element named <literal>&lt;<userinput>xxxxx</userinput>Code></literal> based on a code type:</para>
<itemizedlist>
<listitem><para><literal>@listName</literal> mapped to genericode <literal>&lt;LongName></literal> (first occurrence) using the property <literal>LongName</literal></para>
</listitem>
<listitem><para><literal>@listID</literal> mapped to genericode <literal>&lt;LongName @Identifier='listID'></literal>  or just <literal>&lt;LongName></literal> (first occurrence) using the property <literal>listID</literal></para>
</listitem>
<listitem><para><literal>@listVersionID</literal> mapped to genericode <literal>&lt;Version></literal> using the property <literal>Version</literal></para>
</listitem>
<listitem><para><literal>@listSchemeURI</literal> mapped to genericode <literal>&lt;CanonicalVersionUri></literal> using the property <literal>CanonicalVersionUri</literal></para>
</listitem>
<listitem><para><literal>@listURI</literal> mapped to genericode <literal>&lt;LocationUri></literal> using the property <literal>LocationUri</literal></para>
</listitem>
<listitem><para><literal>@listAgencyName</literal> mapped to genericode <literal>&lt;Agency>&lt;LongName></literal> using the property <literal>AgencyLongName</literal></para>
</listitem>
<listitem><para><literal>@listAgencyID</literal> mapped to genericode <literal>&lt;Agency>&lt;Identifier></literal> using the property <literal>AgencyID</literal></para>
</listitem>
</itemizedlist>
</listitem>
<listitem><para>
for an element named <literal>&lt;<userinput>yyyyy</userinput>ID></literal> based on an identifier type:</para>
<itemizedlist>
<listitem><para><literal>@schemeName</literal> mapped to genericode <literal>&lt;LongName> (first occurrence)</literal> using the property <literal>LongName</literal></para>
</listitem>
<listitem><para><literal>@schemeVersionID</literal> mapped to genericode <literal>&lt;Version></literal> using the property <literal>Version</literal></para>
</listitem>
<listitem><para><literal>@schemeURI</literal> mapped to genericode <literal>&lt;CanonicalVersionUri></literal> using the property <literal>CanonicalVersionUri</literal></para>
</listitem>
<listitem><para><literal>@schemeDataURI</literal> mapped to genericode <literal>&lt;LocationUri></literal> using the property <literal>LocationUri</literal></para>
</listitem>
<listitem><para><literal>@schemeAgencyName</literal> mapped to genericode  <literal>&lt;Agency>&lt;LongName></literal>AgencyLongName</para>
</listitem>
<listitem><para><literal>@schemeAgencyID</literal> mapped to genericode <literal>&lt;Agency>&lt;Identifier></literal>AgencyID</para>
</listitem>
</itemizedlist>
</listitem>
<listitem><para>
for all other attributes and elements defined by UBL</para>
<itemizedlist>
<listitem><para>
it is assumed there is no meta data that describes the value set from which an information item's values are constrained that is suitable for checking against the meta data found in value set</para>
</listitem>
</itemizedlist>
</listitem>
</itemizedlist>
  <xs:param name="item">
    <para>
      (deprecated) The normalized @item attribute from the association
      (for namespace-safe reasons, $item should not be used).
    </para>
  </xs:param>
  <xs:param name="list">
    <para><literal>&lt;ValueList></literal> node from the association.</para>
  </xs:param>
  <xs:param name="attr">
    <para>true/false that the item being tested is an attribute.</para>
  </xs:param>
  <xs:param name="name">
    <para>the local name of the addressed item (without a prefix).</para>
  </xs:param>
  <xs:param name="ns">
    <para>the namespace of the addressed item (not the prefix).</para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:document-model-metadata">
  <xsl:param name="item"/>
  <xsl:param name="list"/>
  <xsl:param name="attr"/>
  <xsl:param name="name"/>
  <xsl:param name="ns"/>
  <!--this information item might be a code-->
  <xsl:variable name="prefixCode">
    <xsl:call-template name="ccs:truncateCodeIDPrefix">
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="term" select="'Code'"/>
    </xsl:call-template>
  </xsl:variable>
  <!--this information item might be an ID-->
  <xsl:variable name="prefixID">
    <xsl:call-template name="ccs:truncateCodeIDPrefix">
      <xsl:with-param name="name" select="$name"/>
      <xsl:with-param name="term" select="'ID'"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:choose>
    <xsl:when test="$attr">
      <!--this is an attribute, and UBL has only selected meta data for atts-->
      <xsl:choose>
        <xsl:when test="$name='currencyID'">
          <xsl:call-template name="ccs:check-list-property">
            <xsl:with-param name="metaname" 
                                         select="'currencyCodeListVersionID'"/>
            <xsl:with-param name="property" select="'Version'"/>
            <xsl:with-param name="list"     select="$list"/>
            <xsl:with-param name="itemattr" select="$attr"/>
            <xsl:with-param name="metaattr" select="true()"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:when test="$name='unitCode'">
          <xsl:call-template name="ccs:check-list-property">
            <xsl:with-param name="metaname" select="'unitCodeListID'"/>
            <xsl:with-param name="property" select="'ID'"/>
            <xsl:with-param name="list"     select="$list"/>
            <xsl:with-param name="itemattr" select="$attr"/>
            <xsl:with-param name="metaattr" select="true()"/>
          </xsl:call-template>
          <xsl:call-template name="ccs:check-list-property">
            <xsl:with-param name="metaname" select="'unitCodeListVersionID'"/>
            <xsl:with-param name="property" select="'Version'"/>
            <xsl:with-param name="list"     select="$list"/>
            <xsl:with-param name="itemattr" select="$attr"/>
            <xsl:with-param name="metaattr" select="true()"/>
          </xsl:call-template>
          <xsl:call-template name="ccs:check-list-property">
            <xsl:with-param name="metaname" select="'unitCodeListAgencyID'"/>
            <xsl:with-param name="property" select="'AgencyID'"/>
            <xsl:with-param name="list"     select="$list"/>
            <xsl:with-param name="itemattr" select="$attr"/>
            <xsl:with-param name="metaattr" select="true()"/>
          </xsl:call-template>
          <xsl:call-template name="ccs:check-list-property">
            <xsl:with-param name="metaname" select="'unitCodeListAgencyName'"/>
            <xsl:with-param name="property" select="'AgencyLongName'"/>
            <xsl:with-param name="list"     select="$list"/>
            <xsl:with-param name="itemattr" select="$attr"/>
            <xsl:with-param name="metaattr" select="true()"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:when test="$name='mimeCode'">
          <!--no meta data for this known attribute item-->
        </xsl:when>
        <xsl:otherwise>
          <!--no meta data for this unknown attribute item-->
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>
    <xsl:when test="substring-after($name,$prefixCode)='Code'">
      <!--the information item ends in 'Code'-->
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'listID'"/>
        <xsl:with-param name="property" select="'ID'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'listAgencyID'"/>
        <xsl:with-param name="property" select="'AgencyID'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'listAgencyName'"/>
        <xsl:with-param name="property" select="'AgencyLongName'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'listName'"/>
        <xsl:with-param name="property" select="'LongName'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'listVersionID'"/>
        <xsl:with-param name="property" select="'Version'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'listURI'"/>
        <xsl:with-param name="property" select="'LocationUri'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'listSchemeURI'"/>
        <xsl:with-param name="property" select="'CanonicalVersionUri'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
    </xsl:when>
    <xsl:when test="substring-after($name,$prefixID)='ID'">
      <!--the information item ends in 'ID'-->
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'schemeAgencyID'"/>
        <xsl:with-param name="property" select="'AgencyID'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'schemeAgencyName'"/>
        <xsl:with-param name="property" select="'AgencyLongName'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'schemeName'"/>
        <xsl:with-param name="property" select="'LongName'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'schemeVersionID'"/>
        <xsl:with-param name="property" select="'Version'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'schemeDataURI'"/>
        <xsl:with-param name="property" select="'LocationUri'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
      <xsl:call-template name="ccs:check-list-property">
        <xsl:with-param name="metaname" select="'schemeURI'"/>
        <xsl:with-param name="property" select="'CanonicalVersionUri'"/>
        <xsl:with-param name="list"     select="$list"/>
        <xsl:with-param name="itemattr" select="$attr"/>
        <xsl:with-param name="metaattr" select="true()"/>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <!--there is no meta data associated with the information item-->
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xs:template>
  <para>
    Return the text before the last occurrence of the term in the name, if
    the name ends with the term.
  </para>
  <xs:param name="name">
    <para>The information item name being truncated.</para>
  </xs:param>
  <xs:param name="term">
    <para>The term being searched for.</para>
  </xs:param>
  <xs:param name="prefix">
    <para>The portion of text before the term being built up.</para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:truncateCodeIDPrefix">
  <xsl:param name="name"/>
  <xsl:param name="term"/>
  <xsl:param name="prefix"/>
  <xsl:if test="contains($name,$term)">
    <xsl:choose>
      <xsl:when test="substring-after($name,$term)=''">
        <xsl:value-of select="concat($prefix,substring-before($name,$term))"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="ccs:truncateCodeIDPrefix">
          <xsl:with-param name="name" 
                          select="substring-after($name,$term)"/>
          <xsl:with-param name="term" select="$term"/>
          <xsl:with-param name="prefix"
              select="concat($prefix,substring-before($name,$term),$term)"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:if>
</xsl:template>

</xsl:stylesheet>