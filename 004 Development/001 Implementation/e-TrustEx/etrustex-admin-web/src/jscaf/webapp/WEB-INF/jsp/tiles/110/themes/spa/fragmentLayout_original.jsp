<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>

<%-- jSCAF 1.10+ --%>

<tiles:useAttribute name="spaRootUrl" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="fragmentName" classname="java.lang.String" scope="request" ignore="true"/>

<%
	String pageUrl = spaRootUrl + "/sub/" + fragmentName + ".jspf";
%>

<jsp:include page="<%=pageUrl%>"/>