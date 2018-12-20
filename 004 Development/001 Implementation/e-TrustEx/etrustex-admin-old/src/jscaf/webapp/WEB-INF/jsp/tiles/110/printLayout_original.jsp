<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>

<!--[if IE 7]><html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if IE 9]><html class="ie ie9" lang="en"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--><html lang="en"> <!--<![endif]-->

<tiles:useAttribute name="defaultPagePath" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="module" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="pageName" classname="java.lang.String" scope="request" ignore="true"/>

<%
    String s = module;
    String moduleOut = s.replaceAll("\\.", "/");
    String pageUrl = defaultPagePath + "/" + moduleOut + "/" + pageName + ".jsp";
    String pageI18nUrl = defaultPagePath + "/" + moduleOut + "/" + pageName + "-i18n.jsp";
%>

<%-- ==========HEAD============= --%>
<jsp:include page="themes/common/head/common_head_print.jsp"/>
<%-- =========================== --%>

<body style="background:#fff;">
<div id="main" class="clear hidden">
    <div id="main-content" class="clear">
		<jsp:include page="<%=pageUrl%>"/>
    </div>
</div>
<br>
</body>

<%-- ============SCRIPTS============= --%>
<jsp:include page="themes/common/scripts/common_scripts.jsp"/>
<%-- ================================ --%>

<%-- ===== jSCAF APP SETTINGS and i18n RESOURCES ====== --%>
<jsp:include page="../common_app_data.jsp"/>   
<%-- ================================================== --%>
</html>


