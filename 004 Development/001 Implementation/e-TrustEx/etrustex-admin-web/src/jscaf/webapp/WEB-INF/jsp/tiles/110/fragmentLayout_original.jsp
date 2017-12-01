<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>

<%-- jSCAF 1.10+ --%>

<tiles:useAttribute name="defaultPagePath" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="module" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="fragmentName" classname="java.lang.String" scope="request" ignore="true"/>

<%
	String moduleOut = module.replaceAll("\\.", "/");
	String fragmentNameOut = fragmentName.replaceAll("\\.", "/");
	String pageUrl = defaultPagePath + "/" + moduleOut + "/fragments/" + fragmentNameOut + ".jspf";
%>

<jsp:include page="<%=pageUrl%>"/>