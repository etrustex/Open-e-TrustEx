<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="../../xslstyle/xslstyle-docbook.xsl"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.CraneSoftwrights.com/ns/xslstyle"
                xmlns:ccs="urn:x-Crane:ss:Crane-Constraints2Schematron"
                exclude-result-prefixes="xs ccs"
                version="1.0">
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

<!--the external expressions of code lists are in genericode-->
<xsl:import href="Crane-genericode-CodeList.xsl"/>

<!--the instance contexts of items are based on the UBL model-->
<xsl:import href="Crane-UBL-Metadata.xsl"/>

<!--the main logic of the transformation is independent of both-->
<xsl:import href="Crane-Constraints2Schematron.xsl"/>

<xs:doc info="$Id: Crane-UBL-genericode2Schematron.xsl,v 1.2 2007/11/26 23:02:07 G. Ken Holman Exp $"
        filename="Crane-UBL-genericode2Schematron.xsl" internal-ns="ccs" 
        global-ns="xs" vocabulary="DocBook">
  <xs:title>Crane's CVA to Schematron for UBL and genericode</xs:title>
  <para>
     A combination of stylesheets to convert an OASIS code list context
     assocation file for UBL instances and Genericode-expressed codelists.
  </para>
</xs:doc>

<xs:variable>
  <para>
    This string is added to the resulting Schematron assertion file to indicate
    which combination of stylesheets was used to create the Schematron schema.
  </para>
</xs:variable>
<xsl:variable name="ccs:comment">
  This implementation supports UBL metadata and genericode code lists.
</xsl:variable>

</xsl:stylesheet>