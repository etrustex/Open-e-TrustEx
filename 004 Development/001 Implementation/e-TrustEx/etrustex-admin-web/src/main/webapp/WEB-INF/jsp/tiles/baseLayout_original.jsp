<!DOCTYPE HTML>
<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="@tiles.uri@" prefix="tiles" %>
<%@ taglib prefix="j" tagdir="/WEB-INF/tags/jscaf" %>

<tiles:useAttribute name="pageTitle" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="pageIconClass" classname="java.lang.String" scope="request" ignore="true"/>

<tiles:useAttribute name="pageHelpTitle" classname="java.lang.String" scope="request" ignore="true"/>
<tiles:useAttribute name="pageHelpPdfFilename" classname="java.lang.String" scope="request" ignore="true"/>

<!--[if IE 7]><html class="ie ie7" lang="en"> <![endif]-->
<!--[if IE 8]><html class="ie ie8" lang="en"> <![endif]-->
<!--[if IE 9]><html class="ie ie9" lang="en"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--><html lang="en"> <!--<![endif]-->


<%-- TODO Remove this global var. Temporary fix while $._getContextPath() returns '..'--%>
<script type="text/javascript">
    var contextPath = '<%=request.getContextPath()%>';
</script>

<%-- ==========HEAD============= --%>
<tiles:@tiles.insert.attribute@="head"/>
<%-- =========================== --%>

<body>
<div id="main" class="hidden">
    <%-- ==========HEADER================ --%>
    <tiles:@tiles.insert.attribute@="header"/>
    <%-- ================================ --%>
    <div id="page-title-wrapper" class="clear">
        <div class="left-content">
            <h1 id="page-title">
                <j:bean_message message_key="${pageTitle}"/>
            </h1>
            <tiles:@tiles.insert.attribute@="pageHelpButton" ignore="true"/>
        </div>
        <div class="middle-content">
            <tiles:@tiles.insert.attribute@="pageMiddleContentTop" ignore="true"/>
        </div>
        <div class="right-content">
            <tiles:@tiles.insert.attribute@="pageRightContentTop" ignore="true"/>
        </div>
    </div>
    <div class="cl"></div>
    <div id="main-content">
	    <%-- ===========BODY================= --%>
        <tiles:@tiles.insert.attribute@="pageContent"/>
        <%-- ================================ --%>
        <div class="cl"></div>
    </div>
    <div class="cl"></div>
    <%-- ============FOOTER============== --%>
    <tiles:@tiles.insert.attribute@="footer"/>
    <%-- ================================ --%>
    <div class="cl"></div>
</div>
<div class="cl"></div>
</body>
<%-- ============SCRIPTS============= --%>
<jsp:include page="common_scripts.jsp"/>
<%-- ================================ --%>

<%-- ============jSCAF APP SETTINGS============= --%>
<%-- <jsp:include page="common_app_settings.jsp"/> --%>
<%-- =========================================== --%>

<%-- ============jSCAF APP SETTINGS============= --%>
<%-- <jsp:include page="common_app_messages.jsp"/> --%>
<%-- =========================================== --%>

</html>
