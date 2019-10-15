<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>
<%@ page import="java.util.ResourceBundle" %>

<jsp:useBean id="pageHelpTitle" type="java.lang.String" scope="request"/>
<jsp:useBean id="pageHelpPdfFilename" type="java.lang.String" scope="request"/>

<%
String pageHelpTitleVar=ResourceBundle.getBundle("ApplicationRessources").getString(pageHelpTitle);
String pageHelpPdfFilenameVar=ResourceBundle.getBundle("ApplicationRessources").getString(pageHelpPdfFilename);
%>

<a class="JS_open-dialogContent-pdf fl"
   dialogTitle="<%=pageHelpTitleVar%>"
   pdfFileName="helpSheets/<%=pageHelpPdfFilenameVar%>">
   	<i class="icon size24 white icon-ft-help"></i>
</a>
