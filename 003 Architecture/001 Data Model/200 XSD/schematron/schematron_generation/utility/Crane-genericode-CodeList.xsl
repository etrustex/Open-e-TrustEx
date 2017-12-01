<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
              xmlns:g-old="http://genericode.org/2006/ns/CodeList/0.4/"
              xmlns:g="http://docs.oasis-open.org/codelist/ns/genericode/1.0/"
                xmlns:xs="http://www.CraneSoftwrights.com/ns/xslstyle"
                xmlns:ccs="urn:x-Crane:ss:Crane-Constraints2Schematron"
                version="1.0"
                exclude-result-prefixes="g g-old xs ccs">

<xs:doc info="$Id: Crane-genericode-CodeList.xsl,v 1.5 2008/11/11 16:33:10 gkholman Exp $"
        filename="Crane-genericode-CodeList.xsl" internal-ns="ccs" 
        global-ns="xs" vocabulary="DocBook">
  <xs:title>CVA support for genericode list-level definitions</xs:title>
  <para>
    Support for an implementation of the OASIS context/value association files
    of genericode files.
  </para>
  <para>
    This fragment supports external code list expressions of code list values
    written using either genericode 0.4 or genericode 1.0, where those files
    use only simple values for keys.
  </para>
  <para>
    This 
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
    Determine the presence of a document with the expected document element.
  </para>
  <para>Returns the string of an error message if there is a problem</para>
  <xs:param  name="valuelist">
    <para>The supplied declaration of the external value list</para>
  </xs:param>
  <xs:param name="doc">
    <para>The root node of the external value list</para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:code-list-confirm-instance">
  <xsl:param name="valuelist"/>
  <xsl:param name="doc"/>
  <xsl:choose>
    <xsl:when test="not($doc/g:CodeList) and not($doc/g-old:CodeList)">
      <xsl:text>Resource for value list "</xsl:text>
      <xsl:value-of select="$valuelist/@xml:id"/>
      <xsl:text>" points to the document element {</xsl:text>
      <xsl:for-each select="$doc/*">
        <xsl:value-of select="concat(namespace-uri(.),'}',local-name(.))"/>
      </xsl:for-each>
      <xsl:text> and not to either {</xsl:text>
      <xsl:value-of select="document('')/*/namespace::g"/>
      <xsl:text>}CodeList or {</xsl:text>
      <xsl:value-of select="document('')/*/namespace::g-old"/>
      <xsl:text>}CodeList: </xsl:text>
      <xsl:value-of select="$valuelist/@uri"/>
    </xsl:when>
    <xsl:when test="not($doc/*/ColumnSet/Key)">
      <xsl:text>No column declared as a key in the value list "</xsl:text>
      <xsl:value-of select="$valuelist/@xml:id"/>
      <xsl:text>": </xsl:text>
      <xsl:value-of select="$valuelist/@uri"/>
    </xsl:when>
    <xsl:when test="string($valuelist/@key) and 
                    not($doc/*/ColumnSet/Key[normalize-space(@Id)=
                                           normalize-space($valuelist/@key)])">
      <xsl:text>Declared key "</xsl:text>
      <xsl:value-of select="normalize-space($valuelist/@key)"/>
      <xsl:text>" for value list "</xsl:text>
      <xsl:value-of select="$valuelist/@xml:id"/>
      <xsl:text>" not found: </xsl:text>
      <xsl:value-of select="$valuelist/@uri"/>
    </xsl:when>
    <xsl:when test="string($valuelist/@key) and 
                    count($doc/*/ColumnSet/Key[normalize-space(@Id)=
                            normalize-space($valuelist/@key)]/ColumnRef) != 1">
      <xsl:text>Declared key "</xsl:text>
      <xsl:value-of select="normalize-space($valuelist/@key)"/>
      <xsl:text>" for value list "</xsl:text>
      <xsl:value-of select="$valuelist/@xml:id"/>
      <xsl:text>" is not limited to a single column reference: </xsl:text>
      <xsl:value-of select="$valuelist/@uri"/>
    </xsl:when>
    <xsl:when test="not(string($valuelist/@key)) and
                    count($doc/*/ColumnSet/Key) != 1">
      <xsl:text>No chosen key for value list "</xsl:text>
      <xsl:value-of select="$valuelist/@xml:id"/>
      <xsl:text>" that references a list containing multiple keys: </xsl:text>
      <xsl:value-of select="$valuelist/@uri"/>
    </xsl:when>
    <xsl:when test="not(string($valuelist/@key)) and
                    count($doc/*/ColumnSet/Key/ColumnRef) != 1">
      <xsl:text>Default key for value list "</xsl:text>
      <xsl:value-of select="$valuelist/@xml:id"/>
      <xsl:text>" is not limited to a single column reference: </xsl:text>
      <xsl:value-of select="$valuelist/@uri"/>
    </xsl:when>
  </xsl:choose>
</xsl:template>

<xs:template>
  <para>
    Build Schematron <literal>&lt;rule></literal> elements using the selection
    of available values.
  </para>
  <para>
    Note the liberal use of newline sequences in the result using 
    <literal>&lt;xsl:text></literal> in order to make the end result legible
    to a reader; these have no effect on the well-formedness of the result or
    the Schematron validation.
  </para>
  <xs:param name="item">
    <para>The XPath axis and node test of the item being tested.</para>
  </xs:param>
  <xs:param name="context">
    <para>The XPath address of the information item being tested.</para>
  </xs:param>
  <xs:param name="values-identifier">
    <para>The identifier token for the value list being cited.</para>
  </xs:param>
  <xs:param name="valuelists-referenced">
    <para>
      The <literal>&lt;ValueList></literal> elements being referenced by the
      context.
    </para>
  </xs:param>
  <xs:param name="context">
    <para>The XPath context of the item with the violation.</para>
  </xs:param>
  <xs:param name="assoc-uri">
    <para>The URI string of the association file being processed.</para>
  </xs:param>
  <xs:param name="mark">
    <para>A name token expressing some characteristic of the context.</para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:code-list-make-rule">
  <xsl:param name="item"/>
  <xsl:param name="context"/>
  <xsl:param name="values-identifier"/>
  <xsl:param name="valuelists-referenced"/>
  <xsl:param name="assoc-uri"/>
  <xsl:param name="mark"/>
  <xsl:variable name="assertion">
    <xsl:for-each select="$valuelists-referenced">
      <xsl:variable name="list" select="."/>
      <xsl:variable name="key" select="normalize-space(@key)"/>
      <xsl:variable name="values" 
                select="document(@uri,.)/*/SimpleCodeList/Row/Value
                                         [@ColumnRef=../../../ColumnSet/
                                          Key[( $key and @Id=$key ) or 
                                              ( not( $key ) and position()=1 )]
                                          /ColumnRef/@Ref]/SimpleValue"/>
      <xsl:if test="$values">
        <!--determine what kind of quoting is needed-->
        <xsl:variable name="any-has-single-quote" 
                      select="$values[contains(.,&quot;'&quot;)]"/>
        <!--this list is tacked on to the others; first gets trimmed later-->
        <xsl:text> or ( ( </xsl:text>
        <!--check for the need for meta data in the comparison-->
        <xsl:for-each select="document(@uri,.)/*">
          <xsl:variable name="metadata">
            <!--check properties based on item's name stem-->
            <xsl:call-template name="ccs:build-metadata-comparison-string">
              <xsl:with-param name="item" select="$item"/>
              <xsl:with-param name="list" select="$list"/>
            </xsl:call-template>
          </xsl:variable>
          <xsl:if test="string($metadata)">
            <!--only include metadata if there are comparisons-->
            <xsl:value-of select="concat($metadata,' ) and ( ')"/>
          </xsl:if>
        </xsl:for-each>
        <!--values of information item for this particular context-->
        <xsl:choose>
          <xsl:when test="$values[contains(.,' ')] or
                          ( $any-has-single-quote and
                            $values[contains(.,'&quot;')] )">
            <!--at least one has a space, or there are ones with each kind
                of quote, so necessary to check each value independently-->
            <xsl:for-each select="$values/../..">
              <!--walk through the rows-->
              <xsl:variable name="row" select="."/>
              <xsl:if test="position()>1"> or </xsl:if>
              <xsl:choose>
                <xsl:when test="contains(.,&quot;'&quot;)">. = "</xsl:when>
                <xsl:otherwise>. = '</xsl:otherwise>
              </xsl:choose>
              <!--build the value from the columns in columnref order-->
              <xsl:for-each select="../../ColumnSet/
                                    Key[( $key and @Id=$key ) or 
                                        ( not( $key ) and position()=1 )]/
                                    ColumnRef">
                <xsl:variable name="keycolref" select="@Ref"/>
                <xsl:for-each select="$row/Value[normalize-space(@ColumnRef)=
                                                 normalize-space($keycolref)]/
                                      SimpleValue">
                  <xsl:call-template name="ccs:check-value-validity">
                    <xsl:with-param name="list" select="$list"/>
                  </xsl:call-template>
                  <xsl:value-of select="."/>
                </xsl:for-each>
              </xsl:for-each>
              <xsl:choose>
                <xsl:when test="contains(.,&quot;'&quot;)">"</xsl:when>
                <xsl:otherwise>'</xsl:otherwise>
              </xsl:choose>
            </xsl:for-each>
          </xsl:when>
          <xsl:otherwise>
            <!--none of the code values have any embedded spaces and in all
                values there may or may not be a double or single quote, so
                check all values with a contains() instead of individually-->
            <xsl:text>not(contains(normalize-space(.),' '))</xsl:text>
            <xsl:text> and contains( </xsl:text>
            <xsl:choose>
              <xsl:when test="$any-has-single-quote">" </xsl:when>
              <xsl:otherwise>' </xsl:otherwise>
            </xsl:choose>
            <xsl:for-each select="$values/../..">
              <!--walk through the rows-->
              <xsl:variable name="row" select="."/>
              <xsl:if test="position()>1">
                <xsl:text> </xsl:text>
              </xsl:if>
              <!--build the value from the columns in columnref order-->
              <xsl:for-each select="../../ColumnSet/
                                    Key[( $key and @Id=$key ) or 
                                        ( not( $key ) and position()=1 )]/
                                    ColumnRef">
                <xsl:variable name="keycolref" select="@Ref"/>
                <xsl:for-each select="$row/Value[normalize-space(@ColumnRef)=
                                                 normalize-space($keycolref)]/
                                      SimpleValue">
                  <xsl:call-template name="ccs:check-value-validity">
                    <xsl:with-param name="list" select="$list"/>
                  </xsl:call-template>
                  <xsl:value-of select="."/>
                </xsl:for-each>
              </xsl:for-each>
            </xsl:for-each>
            <xsl:choose>
              <xsl:when test="$any-has-single-quote"> "</xsl:when>
              <xsl:otherwise> '</xsl:otherwise>
            </xsl:choose>
            <xsl:text>,concat(' ',normalize-space(.),' ') )</xsl:text>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:text> ) )</xsl:text>
      </xsl:if>
    </xsl:for-each>
  </xsl:variable>
  <!--only add a rule for item if there are values against which to compare-->
  <xsl:if test="string($assertion)">
    <xsl:call-template name="ccs:compose-rule">
      <xsl:with-param name="assertion" select="$assertion"/>
      <xsl:with-param name="values-identifier" select="$values-identifier"/>
      <xsl:with-param name="context" select="$context"/>
      <xsl:with-param name="assoc-uri" select="$assoc-uri"/>
      <xsl:with-param name="mark" select="$mark"/>
    </xsl:call-template>
  </xsl:if>
</xsl:template>

<xs:template>
  <para>
    Check the value at the current node is acceptable for the implementation
    of XPath string checks.
  </para>
  <xs:param  name="list">
    <para>The supplied declaration of the external value list</para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:check-value-validity">
  <xsl:param name="list"/>
  <!--in support of above, this is broken out because it has no variables-->
  <xsl:if test="contains(.,&#34;'&#34;) and contains(.,'&#34;')">
    <xsl:message terminate="yes">
      <xsl:text>This implementation does not support a key value </xsl:text>
      <xsl:text>containing both a single quote and a double quote: "</xsl:text>
      <xsl:value-of select="."/>
      <xsl:text>" in list: xmlid="</xsl:text>
      <xsl:value-of select="$list/@xml:id"/>"<xsl:text/>
    </xsl:message>
  </xsl:if>
</xsl:template>

<xs:template>
  <para>
    Compose the code list's metadata value string for the requested property
    from the list-level meta data, or the empty string if no such property is
    available.
  </para>
  <para>
    The current node is the document element of the referenced list.
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
  <xs:param name="address">
    <para>The address of the instance-level meta data value.</para>
  </xs:param>
</xs:template>
<xsl:template name="ccs:code-list-get-property-value">
  <xsl:param name="address"/>
  <xsl:param name="property"/>

  <!--this code implements an XSLT 1 approach to conditional node expressions
      by using a union of many expressions with predicates-->
  <xsl:variable name="code-list-property-value-nodes"
    select="  Identification[$property='ID'][LongName/@Identifer='listID']/
                                                  LongName[@Identifer='listID']
            | Identification[$property='ID'][not(LongName/@Identifer='listID')]
                                                                      /LongName
            | Identification[$property='ShortName']/ShortName
            | Identification[$property='LongName']/LongName
            | Identification[$property='Version']/Version
            | Identification[$property='CanonicalUri']/CanonicalUri
            | Identification[$property='CanonicalVersionUri']/
                                                            CanonicalVersionUri
            | Identification[$property='LocationURI']/LocationURI
            | Identification[$property='AgencyShortName']/Agency/ShortName
            | Identification[$property='AgencyLongName']/Agency/LongName
            | Identification[$property='AgencyID']/Agency/Identifier"/>
  <xsl:call-template name="ccs:code-list-check-property-value">
    <xsl:with-param name="address" select="$address"/>
    <xsl:with-param name="nodes" select="$code-list-property-value-nodes"/>
  </xsl:call-template>
</xsl:template>

</xsl:stylesheet>
