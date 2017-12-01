<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.CraneSoftwrights.com/ns/xslstyle"
                xmlns:ccs="urn:x-Crane:ss:Crane-Constraints2Schematron"
                exclude-result-prefixes="xs ccs"
                version="1.0">

<xs:doc info="$Id: Crane-No-Metadata.xsl,v 1.3 2007/11/26 23:02:07 G. Ken Holman Exp $"
        filename="Crane-No-Metadata" internal-ns="ccs" global-ns="xs"
        vocabulary="DocBook">
  <xs:title>CVA support for no instance-level meta data</xs:title>
  <para>
    This fragment satisfies the template requirements for Crane's CVA to
    Schematron stylesheets, but it does not support recognizing any instance-
    level meta data.
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

  <xsl:choose>
    <xsl:when test="$attr">
      <!--the item being tested is an attribute-->
      <!-- calling check-list-property for every possible meta data item
           arguments:
             address: XPath string to find the meta data
             property: Meta data abstract identifier string
                       (see Crane-Constraints2Schematron.xsl for details)
             list: pass-through of passed parameter
      -->
      <!--
           <xsl:choose>
             <xsl:when test="$ns='namespace' and $name='attrname'">
               <xsl:call-template name="check-list-property">
                 <xsl:with-param name="metans"   select="'nsvalue'"/>
                 <xsl:with-param name="metaname" select="'name'"/>
                 <xsl:with-param name="property" select="'property'"/>
                 <xsl:with-param name="list"     select="$list"/>
                 <xsl:with-param name="itemattr" select="$attr"/>
                 <xsl:with-param name="metaattr" select="true()"/>
               </xsl:call-template>
             </xsl:when>
             <xsl:otherwise>
               <!-no meta data for this unknown attribute item->
             </xsl:otherwise>
           </xsl:choose>
      -->
    </xsl:when>
    <xsl:otherwise>
      <!--the item being tested is an element-->
      <!-- calling check-list-property for every possible meta data item
           arguments:
             address: XPath string to find the meta data
             property: Meta data abstract identifier string
                       (see Crane-Constraints2Schematron.xsl for details)
             list: pass-through of passed parameter
      -->
      <!--
           <xsl:choose>
             <xsl:when test="$ns='namespace' and $name='elemname'">
               <xsl:call-template name="check-list-property">
                 <xsl:with-param name="metans"   select="'nsvalue'"/>
                 <xsl:with-param name="metaname" select="'name'"/>
                 <xsl:with-param name="property" select="'property'"/>
                 <xsl:with-param name="list"     select="$list"/>
                 <xsl:with-param name="itemattr" select="$attr"/>
                 <xsl:with-param name="metaattr" select="true()"/>
               </xsl:call-template>
             </xsl:when>
             <xsl:otherwise>
               <!-no meta data for this unknown element item->
             </xsl:otherwise>
           </xsl:choose>
      -->
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>