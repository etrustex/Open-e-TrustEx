<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib uri="http://eu.europa.ec.digit.jscaf/tags" prefix="j" %>

<!--[if IE 7]><html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if IE 9]><html class="ie ie9" lang="en"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--><html lang="en"> <!--<![endif]-->

<%-- ==========HEAD============= --%>
<tiles:@tiles.insert.attribute@="head"/>
<%-- =========================== --%>

<body style="background:#fff;">
<div id="main" class="clear hidden">
    <div id="main-content" class="clear">
        <%-- BODY --%>
        <%-- ================================ --%>
        <tiles:@tiles.insert.attribute@="pageContent"/>
        <tiles:@tiles.insert.attribute@="pagei18n" ignore="true"/>
        <%-- ================================ --%>
    </div>
</div>
<br>
</body>

<%-- ============SCRIPTS============= --%>
<jsp:include page="common_scripts.jsp"/>
<%-- ================================ --%>

<%-- ===== jSCAF APP SETTINGS and i18n RESOURCES ====== --%>
<jsp:include page="common_app_data.jsp"/>   
<%-- ================================================== --%>
</html>


