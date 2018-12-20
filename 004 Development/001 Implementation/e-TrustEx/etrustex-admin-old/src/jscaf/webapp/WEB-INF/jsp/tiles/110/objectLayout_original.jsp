<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>

<%-- jSCAF 1.10+ --%>

<!--[if IE 7]><html class="ie ie7" lang="en" style="background:none transparent;"> <![endif]-->
<!--[if IE 8]><html class="ie ie8" lang="en" style="background:none transparent;"> <![endif]-->
<!--[if IE 9]><html class="ie ie9" lang="en" style="background:none transparent;"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--><html lang="en" style="background:none transparent;"> <!--<![endif]-->


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
<jsp:include page="themes/common/head/common_head_object.jsp"/>
<%-- =========================== --%>

<body style="background: transparent; overflow:hidden !important; margin:0;">
<div id="main" class="clear">
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
<div id="jscaf_app_settings"

    data-context-path="<%=request.getContextPath()%>"
    data-language="<j:message key="selected.language"/>"
    data-has-page-inner-fragment=false
    data-is-flat-theme-active=false
    data-is-bootstrap-active=true
    data-is-block-on-load-active=false
    data-is-light-initialisation-active=true
></div>
<%-- ================================================== --%>
</html>