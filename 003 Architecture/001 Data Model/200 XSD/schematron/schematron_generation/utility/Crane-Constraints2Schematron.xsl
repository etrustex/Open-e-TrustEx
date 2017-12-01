<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:sch="http://purl.oclc.org/dsdl/schematron"
                xmlns:vlc=
      "http://docs.oasis-open.org/codelist/ns/ContextValueAssociation/cd2-1.0/"
                xmlns:xs="http://www.CraneSoftwrights.com/ns/xslstyle"
                xmlns:ccs="urn:x-Crane:ss:Crane-Constraints2Schematron"
                version="1.0"
                exclude-result-prefixes="vlc xs ccs">

<xs:doc info="$Id: Crane-Constraints2Schematron.xsl,v 1.8 2008/11/11 16:32:40 gkholman Exp $"
        filename="Crane-Constraints2Schematron.xsl" internal-ns="ccs" 
        global-ns="xs" vocabulary="DocBook">
  <xs:title>Implementation of context/value association to Schematron</xs:title>
  <para>
     A Schematron-based implementation of value validation supporting the
     OASIS Code List Representation Technical Committee specification of
     context/value association files.  This implemenation assumes an
     unspecified query binding or a query Binding of "xslt" as representing
     any valid XPath 1.0 pattern defined by XSLT 1.0 production [1].
  </para>
  <para>
     This stylesheet calls out to external template rules to accommodate the
     naming of metadata and the format of external code list enumerations,
     thus it is not hardwired to any particular document vocabulary or to the
     genericode representation of code lists.
  </para>
  <para>
     It should not be necessary to modify this file in order to support
     any definition of business document meta data or value list vocabulary.
  </para>
  <para>
     Implementation note: literal result elements are not used in this fragment
     because of the desire to emit Schematron elements in the default namespace
     while needing the namespace declared for matching purposes.
  </para>
  <para>
     Implementation restriction:  at this time this script is checking
     individual code list association files for having non-conflicting prefixes
     for namespaces, but it is not checking the entire suite of prefixes for
     namespaces for every included file for unique usage of prefix values in
     the generated result.  This may go unreported and cause a problem in the
     Schematron processing.
  </para>
  <programlisting>
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
</programlisting>
</xs:doc>

<xs:variable>
  <para>Makes namespace work more succinct in the stylesheet.</para>
</xs:variable>
<xsl:variable name="ccs:vlc-namespace" select="document('')/*/namespace::vlc"/>
<xs:variable>
  <para>Makes namespace work more succinct in the stylesheet.</para>
</xs:variable>
<xsl:variable name="ccs:sch-namespace" select="document('')/*/
                                                namespace::*[name(.)='']"/>

<xs:key>
  <para>Keeps code lists handy for reference.</para>
</xs:key>
<xsl:key name="ccs:valuelists" match="vlc:ValueList" use="@xml:id"/>

<xs:variable>
  <para>
    This remembers the association file root node for the initial file only.
  </para>
</xs:variable>
<xsl:variable name="ccs:assoc-file" select="document('',/)"/>
<xs:variable>
  <para>
    This sets the URI of the initial file to the empty string since it cannot
    be known automatically.
  </para>
</xs:variable>
<xsl:variable name="ccs:assoc-uri"/>

<xs:output>
  <para>
    This makes the resulting file easy to view or to edit with an editor that
    does not support UTF-8 encoding.
  </para>
</xs:output>
<xsl:output encoding="ISO-8859-1"/>

<xs:template>
  <para>
    This is the processing of the root node of all CVA files being processed
    (which initially is the first one but this also triggers for the root of
    referenced CVA files).
  </para>
  <xs:param name="assoc-file">
    <para>The document node of the CVA file being processed.</para>
  </xs:param>
  <xs:param name="assoc-uri">
    <para>The URI string for the CVA file being processed.</para>
  </xs:param>
</xs:template>
<xsl:template match="/">
  <xsl:param name="assoc-file" select="$ccs:assoc-file"/>
  <xsl:param name="assoc-uri" select="$ccs:assoc-uri"/>
  <!--prevent stylesheet abuse-->
  <xsl:if test="not($assoc-file/vlc:ValueListConstraints)">
    <xsl:message terminate="yes">
      <xsl:if test="$assoc-uri">
        <xsl:text>Problem processing file '</xsl:text>
        <xsl:value-of select="$assoc-uri"/>
        <xsl:text>': </xsl:text>
      </xsl:if>
      <xsl:text>Expected the document element {</xsl:text>
      <xsl:value-of select="$ccs:vlc-namespace"/>
      <xsl:text>}ValueListConstraints, but instead have {</xsl:text>
      <xsl:value-of select="concat( namespace-uri(*),'}',local-name(*) )"/>
    </xsl:message>
  </xsl:if>
  <xsl:if test="not($assoc-uri)">
    <!--first time around, check the include tree for infinite loops-->
    <xsl:call-template name="ccs:check-infinite-loops"/>
  </xsl:if>
  <!--check given instance for inconsistencies-->
  <xsl:call-template name="ccs:check-inconsistencies"/>
  <xsl:choose>
    <xsl:when test="$assoc-uri">
      <!--this has been invoked by an Include directive, so just add rules-->
      <xsl:call-template name="ccs:add-rules">
        <xsl:with-param name="assoc-file" select="$assoc-file"/>
        <xsl:with-param name="assoc-uri" select="$assoc-uri"/>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <!--create the pattern-->
      <xsl:element name="pattern" 
                   namespace="http://purl.oclc.org/dsdl/schematron">
        <xsl:attribute name="id">
          <xsl:value-of select="$assoc-file/vlc:ValueListConstraints/@name"/>
        </xsl:attribute>
        <xsl:call-template name="ccs:add-rules">
          <xsl:with-param name="assoc-file" select="$assoc-file"/>
          <xsl:with-param name="assoc-uri" select="$assoc-uri"/>
        </xsl:call-template>
      </xsl:element>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<!--========================================================================-->
<xs:doc>
  <xs:title>Templates in regard of creating Schematron rules.</xs:title>
</xs:doc>

<xs:template>
  <para>Add the rules from the context association file to the result.</para>
  <xs:param name="assoc-file">
    <para>The document node of the association file being processed.</para>
  </xs:param>
  <xs:param name="assoc-uri">
    <para>The URI string of the association file being processed.</para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:add-rules">
  <xsl:param name="assoc-file" select="$ccs:assoc-file"/>
  <xsl:param name="assoc-uri" select="$ccs:assoc-uri"/>
  <xsl:text>
</xsl:text>
  <xsl:if test="not($assoc-uri)">
    <!--$comment must be supplied by an external fragment; not defined here-->
    <xsl:comment><xsl:value-of select="$ccs:comment"/></xsl:comment><xsl:text>
</xsl:text>
  </xsl:if>
  <xsl:comment>
    Start of synthesis of rules from code list context associations.
<xsl:if test="normalize-space($assoc-file/*/vlc:Title)">
  <!--this will help identify the sets of rules-->
  <xsl:value-of select="$assoc-file/*/vlc:Title"/>
  <xsl:text>
</xsl:text>
</xsl:if>
<xsl:if test="normalize-space($assoc-file/*/@version)">
  <!--this will help identify the sets of rules-->
  <xsl:value-of select="$assoc-file/*/@version"/>
  <xsl:text>
</xsl:text>
</xsl:if>
<!--determine end-user namespaces by ignoring expected namespaces-->
<xsl:variable name="ns" 
              select="$assoc-file//namespace::*[name(.) != 'xml' and 
                        . != $ccs:vlc-namespace and . != $ccs:sch-namespace]"/>
<xsl:if test="$ns">
    Required namespace declarations as indicated in this set of rules:

<xsl:for-each select="$ns">
  <xsl:if test="generate-id(.)=generate-id($ns[name(.)=name(current())][1])">
    <xsl:value-of select="concat('&lt;ns prefix=&quot;',name(.),
                          '&quot; uri=&quot;',.,'&quot;/>')"/>
      <xsl:text>
</xsl:text>
  </xsl:if>
</xsl:for-each>
      <xsl:text>
</xsl:text>
</xsl:if>
</xsl:comment><xsl:text>
</xsl:text>
  <xsl:apply-templates select="$assoc-file/*/vlc:Contexts/vlc:Context">
    <xsl:with-param name="assoc-uri" select="$assoc-uri"/>
  </xsl:apply-templates>
  <xsl:comment>
    End of synthesis of rules from code list context associations.
</xsl:comment><xsl:text>
</xsl:text>
  <!--copy any indentation that was at the end of the rules-->
  <xsl:copy-of select="node()[last()][self::text()]"/>

  <xsl:for-each select="$assoc-file/*/vlc:Include">
    <xsl:sort select="position()" order="descending" data-type="number"/>
    <xsl:call-template name="ccs:add-rules">
      <xsl:with-param name="assoc-file" select="document(@uri,.)"/>
      <xsl:with-param name="assoc-uri" select="@uri"/>
    </xsl:call-template>
  </xsl:for-each>
</xsl:template>

<xs:template>
  <para>
    Create one rule for each context in the association file, gathering the
    information from all of the lists referenced.
  </para>
  <xs:param name="assoc-uri">
    <para>The URI string of the association file being processed.</para>
  </xs:param>
  <xs:param name="valuelist-references">
    <para>
      The string of identifiers of <literal>&lt;ValueList></literal> elements
      that have not net been gathered.
    </para>
  </xs:param>
  <xs:param name="valuelists-referenced">
    <para>
      The node set of document nodes of external lists referenced.
    </para>
  </xs:param>
</xs:template>
<xsl:template match="vlc:Context" name="ccs:gather-context">
  <xsl:param name="assoc-uri"/>
  <xsl:param name="valuelist-references" 
             select="concat( normalize-space(@values), ' ' )"/>
  <xsl:param name="valuelists-referenced" select="/.."/>
  <!--process the context-->
  <xsl:choose>
    <xsl:when test="contains( $valuelist-references,' ' )">
      <!--gather up code list references for the given context-->
      <xsl:call-template name="ccs:gather-context">
        <xsl:with-param  name="valuelist-references"
                         select="substring-after($valuelist-references,' ')"/>
        <xsl:with-param  name="valuelists-referenced"
                         select="$valuelists-referenced | 
           key('ccs:valuelists',substring-before($valuelist-references,' '))"/>
        <xsl:with-param name="assoc-uri" select="$assoc-uri"/>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <!--now have the union set of all referenced in given context-->
      <xsl:variable name="context">
        <!--assume the context is the item address, but address overrides it-->
        <xsl:choose>
          <xsl:when test="@address">
            <xsl:value-of select="@address"/>
          </xsl:when>
          <xsl:when test="@scope">
            <xsl:value-of select="concat(@scope,'//',@item)"/>
          </xsl:when>
          <xsl:otherwise><xsl:value-of select="@item"/></xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <!--make the rules for this context-->
      <xsl:call-template name="ccs:code-list-make-rule">
        <xsl:with-param name="item" select="@item"/>
        <xsl:with-param name="context" select="$context"/>
        <xsl:with-param name="valuelists-referenced" 
                        select="$valuelists-referenced"/>
        <xsl:with-param name="values-identifier" 
                        select="normalize-space(@values)"/>
        <xsl:with-param name="assoc-uri" select="$assoc-uri"/>
        <xsl:with-param name="mark" select="normalize-space(@mark)"/>
      </xsl:call-template>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xs:template>
  <para>Compose a Schematron rule expressing the asserted constraint.</para>
  <xs:param name="assertion">
    <para>
      The text of the assembled assertion (prefixed with 
      "<literal> or </literal>").
  </para>
  </xs:param>
  <xs:param name="values-identifier">
    <para>The identifier token for the value list being cited.</para>
  </xs:param>
  <xs:param name="context">
    <para>The XPath context of the item being tested.</para>
  </xs:param>
  <xs:param name="assoc-uri">
    <para>The URI string of the association file being processed.</para>
  </xs:param>
  <xs:param name="mark">
   <para>The name token of a characterization of the rule and assertion.</para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:compose-rule">
  <xsl:param name="assertion"/>
  <xsl:param name="values-identifier"/>
  <xsl:param name="context"/>
  <xsl:param name="assoc-uri"/>
  <xsl:param name="mark"/>
  <xsl:text>
</xsl:text>
    <xsl:element name="rule" namespace="http://purl.oclc.org/dsdl/schematron">
      <xsl:attribute name="context">
        <xsl:value-of select="$context"/>
      </xsl:attribute>
      <xsl:if test="normalize-space($mark)">
        <xsl:attribute name="flag">
          <xsl:value-of select="$mark"/>
        </xsl:attribute>
      </xsl:if>
      <xsl:text>
  </xsl:text>
      <!--indicate values that must be true-->
      <!--the first " or " in the string is not required, so trim it off-->
      <xsl:element name="assert" 
                   namespace="http://purl.oclc.org/dsdl/schematron">
        <xsl:attribute name="test">
          <xsl:value-of select="substring($assertion,5)"/>
        </xsl:attribute>
        <xsl:if test="normalize-space($mark)">
          <xsl:attribute name="flag">
            <xsl:value-of select="$mark"/>
          </xsl:attribute>
        </xsl:if>
        <!--message to report if the test fails-->
        <xsl:call-template name="ccs:compose-violation-report">
         <xsl:with-param name="values-identifier" select="$values-identifier"/>
         <xsl:with-param name="assoc-uri" select="$assoc-uri"/>
         <xsl:with-param name="context" select="$context"/>
        </xsl:call-template>
      </xsl:element><xsl:text>
</xsl:text>
    </xsl:element><xsl:text>
</xsl:text>
</xsl:template>

<xs:template>
  <para>Compose the default report of a context violation.</para>
  <xs:param name="values-identifier">
    <para>The identifier token for the value list being cited.</para>
  </xs:param>
  <xs:param name="assoc-uri">
    <para>The URI string of the association file being processed.</para>
  </xs:param>
  <xs:param name="context">
    <para>The XPath context of the item with the violation.</para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:compose-violation-report">
  <xsl:param name="values-identifier"/>
  <xsl:param name="assoc-uri"/>
  <xsl:param name="context"/>
  <xsl:choose>
    <xsl:when test="normalize-space(vlc:Message) or vlc:Message/*">
      <!--the context includes the desired messages to be displayed-->
      <xsl:apply-templates mode="ccs:sch-only" select="vlc:Message/node()"/>
    </xsl:when>
    <xsl:otherwise>
      <!--a default message is composed to report the violation-->
      <xsl:text>Value supplied '</xsl:text>
      <xsl:element name="value-of" 
                   namespace="http://purl.oclc.org/dsdl/schematron">
        <xsl:attribute name="select">.</xsl:attribute>
      </xsl:element>
      <xsl:text>' is unacceptable for values identified by '</xsl:text>
      <xsl:value-of select="$values-identifier"/>
      <xsl:if test="$assoc-uri">
        <xsl:text>' in association file with uri '</xsl:text>
        <xsl:value-of select="$assoc-uri"/>
      </xsl:if>
      <xsl:text>' in the context '</xsl:text>
      <xsl:value-of select="$context"/>'<xsl:text/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xs:template>
  <para>Copy message content from the CVA file to the Schematron file.</para>
</xs:template>
  <!--when copying the message content from CVA file, trim to Schematron ns-->
<xsl:template mode="ccs:sch-only" match="node()">
  <xsl:copy>
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates/>
  </xsl:copy>
</xsl:template>
<xs:template>
  <para>
    Schematron elements in the message content need to go out without
    a namespace.
  </para>
</xs:template>
<xsl:template mode="ccs:sch-only" match="sch:*">
  <xsl:element name="{local-name(.)}" namespace="{namespace-uri(.)}">
    <xsl:copy-of select="@*"/>
    <xsl:apply-templates/>
  </xsl:element>
</xsl:template>

<!--========================================================================-->
<xs:doc>
  <xs:title>Composing the Schematron assertion strings.</xs:title>
</xs:doc>

<xs:variable>
  <para
    >Most characters not allowed in a part of an attribute or element name 
    (this is not meant to be rigourous, just strong enough for obvious errors);
    assumes the axis is removed and the prefix and name are separate.
  </para>
</xs:variable>
<xsl:variable name="ccs:forbidden"
  >@:`~!#$%^&amp;()+=[]{}\|;"'&lt;>?/</xsl:variable>

<xs:template>
  <para>
    Compose a single comparison string for an information item.
  </para>
  <para>
    This template will call the vocabulary-specific template that knows which
    instance attributes express instance-level meta data about the item's
    list-level attributes.
  </para>
  <xs:param name="item">
    <para>The XPath axis and node test of the item being tested.</para>
  </xs:param>
  <xs:param name="list">
    <para>The CVA value list declaration for the list of allowed values.</para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:build-metadata-comparison-string">
  <xsl:param name="item"/>
  <xsl:param name="list"/>
  <!--get rid of unwanted space-->
  <xsl:variable name="normitem" select="normalize-space($item)"/>
  <!--determine if an attribute is being addressed-->
  <xsl:variable name="attraxis">
    <xsl:choose>
      <xsl:when test="starts-with($normitem,'@')">@</xsl:when>
      <xsl:when test="starts-with($normitem,'attribute::')"
                                                        >attribute::</xsl:when>
    </xsl:choose>
  </xsl:variable>
  <!--true/false the addressed item is an attribute-->
  <xsl:variable name="attr" select="boolean(string($attraxis))"/>
  <!--determine the qualified name of the item-->
  <xsl:variable name="qname" select="substring-after($normitem,$attraxis)"/>
  <!--determine the prefix of the item name-->
  <xsl:variable name="prefix" select="substring-before($qname,':')"/>
  <!--determine the local part of the item name-->
  <xsl:variable name="name">
    <xsl:choose>
      <xsl:when test="contains($qname,':')">
        <xsl:value-of select="substring-after($qname,':')"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$qname"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <!--determine the namespace of the item-->
  <xsl:variable name="ns">
    <!--don't look if the attribute doesn't have a prefix-->
    <xsl:if test="not( $attr and not( $prefix ) )">
      <xsl:value-of select="$item/../namespace::*[name(.)=$prefix]"/>
    </xsl:if>
  </xsl:variable>

  <!--check for any forbidden characters in the item attribute value-->
  <xsl:if test="translate($name,translate($name,$ccs:forbidden,''),'') or
                translate($prefix,translate($prefix,$ccs:forbidden,''),'')">
    <xsl:message terminate="yes">
      <xsl:text>Unexpected character(s) in item= attribute '</xsl:text>
      <xsl:value-of select="$normitem"/>
      <xsl:text>': </xsl:text>
      <xsl:call-template name="ccs:report-current-node-context"/>
    </xsl:message>
  </xsl:if>

  <!--build a string of expressions checking for meta data-->
  <xsl:variable name="properties">
    <xsl:call-template name="ccs:document-model-metadata">
      <xsl:with-param name="item" select="$normitem"/>
      <xsl:with-param name="list" select="$list"/>
      <xsl:with-param name="attr" select="$attr"/>
      <xsl:with-param name="name" select="string($name)"/>
      <xsl:with-param name="ns"   select="string($ns)"/>
    </xsl:call-template>
  </xsl:variable>

  <!--the first " and " in the string is not required, so trim it off-->
  <xsl:value-of select="substring($properties,6)"/>
</xsl:template>

<xs:template>
  <para>
    Compose the portion of the comparison expression for a given meta 
    data property value.
  </para>
  <para>
    This is called by the vocabulary-specific template that knows which
    instance attributes express instance-level meta data about the item's
    list-level attributes.
  </para>
  <para>
    First, any masquerading meta data for the property is checked before
    checking any list-level meta data.  The presence of any masquerading
    meta data for a given property overrides all list-level meta data for
    the property.
  </para>
  <xs:param name="property">
    <para>
      The name of the abstract code list meta data property where
      any meta data value can be equal to the instance value
    </para>
    <variablelist>
      <varlistentry>
        <term><literal>ID</literal></term>
        <listitem>
          <literal>Identification/LongName[@Identifier='listID']</literal>, or if not
          present then <literal>Identification/LongName</literal>
        </listitem>
      </varlistentry>
      <varlistentry>
        <term><literal>ShortName</literal></term>
        <listitem>
          <literal>Identification/ShortName</literal>
        </listitem>
      </varlistentry>
      <varlistentry>
        <term><literal>LongName</literal></term>
        <listitem>
          <literal>Identification/LongName</literal>
        </listitem>
      </varlistentry>
      <varlistentry>
        <term><literal>LongName</literal></term>
        <listitem>
          <literal>Identification/LongName</literal>
        </listitem>
      </varlistentry>
      <varlistentry>
        <term><literal>Version</literal></term>
        <listitem>
          <literal>Identification/Version</literal>
        </listitem>
      </varlistentry>
      <varlistentry>
        <term><literal>CanonicalUri</literal></term>
        <listitem>
          <literal>Identification/CanonicalUri</literal>
        </listitem>
      </varlistentry>
      <varlistentry>
        <term><literal>CanonicalVersionUri</literal></term>
        <listitem>
          <literal>Identification/CanonicalVersionUri</literal>
        </listitem>
      </varlistentry>
      <varlistentry>
        <term><literal>LocationUri</literal></term>
        <listitem>
          <literal>Identification/LocationUri</literal>
        </listitem>
      </varlistentry>
      <varlistentry>
        <term><literal>AlternateFormatLocationUri</literal></term>
        <listitem>
          <literal>Identification/AlternateFormatLocationUri</literal>
        </listitem>
      </varlistentry>
      <varlistentry>
        <term><literal>AgencyID</literal></term>
        <listitem>
          <literal>Identification/Agency/Identifier</literal>
        </listitem>
      </varlistentry>
      <varlistentry>
        <term><literal>AgencyLongName</literal></term>
        <listitem>
          <literal>Identification/Agency/LongName</literal>
        </listitem>
      </varlistentry>
    </variablelist>
  </xs:param>
  <xs:param name="list">
    <para><literal>&lt;ValueList></literal> node from the association.</para>
  </xs:param>
  <xs:param name="itemattr">
    <para>true/false that the item being tested is an attribute.</para>
  </xs:param>
  <xs:param name="metaattr">
    <para>true/false that the meta data value is an attribute.</para>
  </xs:param>
  <xs:param name="metans">
    <para>namespace of the meta data element/attribute in the instance.</para>
  </xs:param>
  <xs:param name="metaname">
    <para>name of the meta data element/attribute.</para>
  </xs:param>
  <xs:param name="address">
    <para>
      (deprecated) only use an explicit XPath address here if the other
      parameters are not sufficient to describe it in the abstract;
      any value here will cause the other parameters to be ignored
    </para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:check-list-property">
  <xsl:param name="property"/>
  <xsl:param name="list"/>
  <xsl:param name="itemattr" select="true()"/>
  <xsl:param name="metaattr" select="true()"/>
  <xsl:param name="metans"/>
  <xsl:param name="metaname"/>
  <xsl:param name="address">
    <!--when not supplied, must compose an address from the other arguments-->
    <xsl:choose>
      <xsl:when test="not($metaattr)">
        <!--the meta data is an element, must be item's parent's child-->
        <xsl:text>../</xsl:text>
      </xsl:when>
      <!--the meta data is an attribute-->
      <xsl:when test="$itemattr">
        <!--the meta data is a sibling attribute of the item attribute-->
        <xsl:text>../@</xsl:text>
      </xsl:when>
      <xsl:otherwise>
        <!--the meta data is an attached attribute of the item element-->
        <xsl:text>@</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
    <!--the axis is done, just name the element or attribute now-->
    <xsl:choose>
      <xsl:when test="$metaattr and not(normalize-space($metans))">
        <!--a non-namespaced attribute is simply addressed by name-->
        <xsl:value-of select="$metaname"/>
      </xsl:when>
      <xsl:otherwise>
        <!--elements and namespaced attributes need uri checking-->
        <xsl:text>*[namespace-uri(.)="</xsl:text>
        <xsl:value-of select="$metans"/>
        <xsl:text>" and local-name(.)="</xsl:text>
        <xsl:value-of select="$metaname"/>
        <xsl:text>"]</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:param>

  <!--determine the requested property is allowed to be requested-->
  <xsl:if test="not(contains(concat(' ID ShortName LongName Version',
                                    ' CanonicalUri CanonicalVersionUri',
                                    ' LocationUri AlternateFormatLocationUri',
                                  ' AgencyShortName AgencyLongName AgencyID '),
                             concat(' ',$property,' ')))">
    <xsl:message terminate="yes">
      <xsl:text>Unexpected code list property: </xsl:text>
      <xsl:value-of select="$property"/>
    </xsl:message>
  </xsl:if>

  <!--determine the meta data from either the masquerade or external file-->
  <!--this code implements an XSLT 1 approach to conditional node expressions
      by using a union of many expressions with predicates-->
  <xsl:variable name="code-list-property-value-nodes"
    select="  $list[$property='ID']/vlc:Identification[vlc:LongName/@Identifer=
                                    'listID']/vlc:LongName[@Identifer='listID']
            | $list[$property='ID']/vlc:Identification[not(vlc:LongName/@Identifer=
                                                        'listID')]/vlc:LongName
            | $list[$property='ShortName']/vlc:Identification/vlc:ShortName
            | $list[$property='LongName']/vlc:Identification/vlc:LongName
            | $list[$property='Version']/vlc:Identification/vlc:Version
            | $list[$property='CanonicalUri']/vlc:Identification/vlc:CanonicalUri
            | $list[$property='CanonicalVersionUri']/vlc:Identification/
                                                        vlc:CanonicalVersionUri
            | $list[$property='LocationUri']/vlc:Identification/vlc:LocationUri
            | $list[$property='AlternateFormatLocationUri']/vlc:Identification/
                                                 vlc:AlternateFormatLocationUri
            | $list[$property='AgencyShortName']/vlc:Identification/vlc:Agency/
                                                                  vlc:ShortName
            | $list[$property='AgencyLongName']/vlc:Identification/vlc:Agency/
                                                                   vlc:LongName
            | $list[$property='AgencyID']/vlc:Identification/vlc:Agency/
                                                              vlc:Identifier"/>
  <xsl:choose>
    <xsl:when test="$code-list-property-value-nodes">
      <!--there are one or more masquerading values for this property-->
      <xsl:call-template name="ccs:code-list-check-property-value">
        <xsl:with-param name="address" select="$address"/>
        <xsl:with-param name="nodes" select="$code-list-property-value-nodes"/>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <!--need to go to the external file for the meta data string-->
      <xsl:call-template name="ccs:code-list-get-property-value">
        <xsl:with-param name="address" select="$address"/>
        <xsl:with-param name="property" select="$property"/>
      </xsl:call-template>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xs:template>
  <para>
    This composes the " and " portion for a given set of meta data values
    which is a collection of " or " portions of each of the values in the
    nodes parameter.
  </para>
  <xs:param name="address">
    <para>The address of the instance-level meta data value.</para>
  </xs:param>
  <xs:param name="nodes">
    <para>
      The nodes of the meta data values either in the masquerading meta data
      or in the list-level meta data.
    </para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:code-list-check-property-value">
  <xsl:param name="address"/>
  <xsl:param name="nodes"/>
  <!--express the boolean expression of a property's value needing to be set;
      note by using a double negative of not(x!=y) instead of (x=y) is required
      so that the test will not fail if an operand is not present.  Using a
      double negative will return true if the operand is not present, and will
      return false if the operand is present but the wrong value.-->
  <xsl:if test="$nodes">
    <!--note that the leading "and" is stripped away before being used-->
    <xsl:text> and ( </xsl:text>
    <xsl:for-each select="$nodes">
      <!--the "or" accommodates specification of multiple possible values-->
      <xsl:if test="position()>1"> or </xsl:if>
      <!--quotes are used for values because URI strings can have apos-->
      <xsl:value-of select="concat('not(',$address,'!=&quot;',.,'&quot;)')"/>
    </xsl:for-each>
    <xsl:text> )</xsl:text>
  </xsl:if>
</xsl:template>

<!--========================================================================-->
<xs:doc>
  <xs:title>Validating the CVA file content business rules</xs:title>
</xs:doc>

<xs:template>
  <para>Consistency checks for user's expression of constraints.</para>
</xs:template>

<xsl:template name="ccs:check-inconsistencies">
  <xsl:variable name="inconsistencies">
    <!--expected binding-->
    <xsl:for-each select="/*/@queryBinding[.!='xslt']">
      <xsl:text>The query binding must be "xslt" and no other value.</xsl:text>
    </xsl:for-each>

    <!--good refs-->
    <xsl:for-each select="/*/vlc:Contexts/
                          vlc:Context[normalize-space(@values)='']">
      <xsl:text>A context cannot have no references to values: </xsl:text>
      <xsl:call-template name="ccs:report-current-node-context"/>
      <xsl:text>
</xsl:text>
    </xsl:for-each>

    <!--defined ids-->
    <xsl:for-each select="/*/vlc:ValueLists/
                          vlc:ValueList[normalize-space(@xml:id)='']">
      <xsl:text>A code list cannot be listed without an xml:id </xsl:text>
      <xsl:text>unique identifier: </xsl:text>
      <xsl:call-template name="ccs:report-current-node-context"/>
      <xsl:text>
</xsl:text>
    </xsl:for-each>

    <!--no duplicate ids-->
    <xsl:for-each select="/*/vlc:ValueLists/
                     vlc:ValueList[count(key('ccs:valuelists',@xml:id))>1][1]">
    <xsl:text>A code list xml:id identifier must be unique (xml:id="</xsl:text>
      <xsl:value-of select="@xml:id"/>
      <xsl:text>"): 
</xsl:text>
      <xsl:for-each select="key('ccs:valuelists',@xml:id)">
        <xsl:text>  </xsl:text>
        <xsl:call-template name="ccs:report-current-node-context"/>
        <xsl:text>
</xsl:text>
      </xsl:for-each>
    </xsl:for-each>

    <!--no missing or conflicting contexts-->
    <xsl:for-each select="/*/vlc:Contexts/vlc:Context">
      <xsl:choose>
        <xsl:when test="@address and @scope">
          <xsl:text>A context cannot have both @address and @scope: </xsl:text>
          <xsl:call-template name="ccs:report-current-node-context"/>
          <xsl:text>
</xsl:text>
        </xsl:when>
      </xsl:choose>
    </xsl:for-each>

    <!--check all document references to point to genericode instances-->
    <xsl:for-each select="/*/vlc:ValueLists/vlc:ValueList">
      <xsl:variable name="doc" select="document(@uri,.)"/>
      <xsl:choose>
        <xsl:when test="not($doc)">
        <xsl:text>Resource not found or cannot be processed as XML: </xsl:text>
          <xsl:value-of select="concat(@uri,': ')"/>
          <xsl:call-template name="ccs:report-current-node-context"/>
          <xsl:text>
</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="ccs:code-list-confirm-instance">
            <xsl:with-param name="valuelist" select="."/>
            <xsl:with-param name="doc" select="$doc"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>

  </xsl:variable>

  <xsl:if test="string($inconsistencies)">
    <xsl:message terminate="yes">
      <xsl:value-of select="$inconsistencies"/>
    </xsl:message>
  </xsl:if>
</xsl:template>

<!--========================================================================-->
<xs:doc>
  <xs:title>Infinite include loop checking</xs:title>
</xs:doc>

<xs:template>
  <para>Begin walk through include tree looking for an infinite loop.</para>
</xs:template>
<xsl:template name="ccs:check-infinite-loops">
  <xsl:variable name="bad-loop">
    <!--generate any messages-->
    <xsl:call-template name="ccs:check-given-include-tree"/>
  </xsl:variable>

  <!--exit if messages-->
  <xsl:if test="string($bad-loop)">
    <xsl:message terminate="yes">
      <xsl:copy-of select="$bad-loop"/>
    </xsl:message>
  </xsl:if>
</xsl:template>

<xs:template>
  <para>Recurse through nested include directives looking for a loop.</para>
  <xs:param name="root-node-ids">
    <para>Collection of document nodes of included CVA files.</para>
  </xs:param>
  <xs:param name="uri">
    <para>
      The association file URI string (not known for invocation CVA file) of
      the included CVA.
    </para>
  </xs:param>
  <xs:param name="last-uri">
    <para>
      The association file URI string (not known for invocation CVA file) of
      the including CVA.
    </para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:check-given-include-tree">
  <xsl:param name="root-node-ids" select="' '"/>
  <xsl:param name="uri" select="'{invocation document}'"/>
  <xsl:param name="last-uri"/>
  <xsl:variable name="this-root-node-id" select="generate-id(document('',/))"/>
  <xsl:choose>
    <xsl:when test="contains( $root-node-ids,
                              concat(' ',$this-root-node-id,' ') )">
      <!--we've ended up pointing to somewhere in our ancestry-->
      <xsl:text>Infinite loop of include statements triggered by </xsl:text>
      <xsl:text/>including '<xsl:value-of select="$uri"/>' from '<xsl:text/>
      <xsl:value-of select="$last-uri"/>'<xsl:text>
</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <!--so far so good, now check down next level of children-->
      <xsl:variable name="past-root-node-ids"
                   select="concat( $root-node-ids, $this-root-node-id, ' ' )"/>
      <xsl:for-each select="document('',/)/*/vlc:Include">
        <xsl:variable name="new-uri" select="@uri"/>
        <xsl:choose>
          <xsl:when test="not(normalize-space(@uri))">
   <xsl:text>Include directive cannot have an empty uri= attribute: </xsl:text>
            <xsl:call-template name="ccs:report-current-node-context"/>
            <xsl:text>
</xsl:text>
          </xsl:when>
          <xsl:otherwise>
            <xsl:for-each select="document($new-uri,.)">
              <xsl:call-template name="ccs:check-given-include-tree">
                <xsl:with-param name="root-node-ids" 
                                select="$past-root-node-ids"/>
                <xsl:with-param name="uri" select="$new-uri"/>
                <xsl:with-param name="last-uri" select="$uri"/>
              </xsl:call-template>
            </xsl:for-each>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<!--========================================================================-->
<xs:doc>
  <xs:title>Diagnostics</xs:title>
</xs:doc>

<xs:template>
  <para>Diagnostic function to report context for errors.</para>
</xs:template>
<xsl:template name="ccs:report-current-node-context">
  <!--start with the XPath address of the element in the context expression-->
  <xsl:for-each select="ancestor-or-self::*">
    <xsl:text/>/<xsl:value-of select="name(.)"/>
    <xsl:variable name="preceding-count" 
                select="count(preceding-sibling::*[name(.)=name(current())])"/>
    <xsl:if test="$preceding-count > 0">
      <xsl:text/>[<xsl:value-of select="$preceding-count + 1"/>]<xsl:text/>
    </xsl:if>
  </xsl:for-each>
  <!--suffix with attribute or namespace node as appropriate-->
  <xsl:if test="not(self::*)">
    <xsl:text>/</xsl:text>
    <xsl:if test="count(../@*) = count(.|../@*)">@</xsl:if>
    <xsl:if test="count(../namespace::*) = count(.|../namespace::*)">
      <xsl:text>namespace::</xsl:text>
    </xsl:if>
    <xsl:value-of select="name(.)"/>
  </xsl:if>
</xsl:template>

</xsl:stylesheet>